package com.muffinmanager.api.muffinmanagerapi.service.auth;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginResponse;
import com.muffinmanager.api.muffinmanagerapi.model.User.RegisterRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.UserEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.GroupEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.PermissionEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.Permissions;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.stats.UserStatsEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.AvailableUserPermissionsDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UpdateUserDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserDetailedDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IGroupRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IPermissionRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserStatsRepository;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IJwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    private IUserStatsRepository userStatsRepository;
    @Autowired
    private IGroupRepository groupRepository;
    @Autowired
    private IPermissionRepository permissionRepository;
    

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getDni(), request.getPassword())); 
        UserEntity user = userRepository.findByDni(request.getDni()).orElseThrow();
        return generateLoginResponse(user);
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        UserEntity user = UserEntity.builder()
            .dni(request.getDni())
            .name(request.getName())
            .secondName(request.getSecondName())
            .password(passwordEncoder.encode(request.getPassword()))
            .groups(request.getGroups())
            .permissions(request.getPermissions())
            .build();
        userRepository.save(user);
        return generateLoginResponse(user);
    }

    private LoginResponse generateLoginResponse(UserEntity user) {
        return !user.isDisabled() ? LoginResponse.builder()
            .id(user.getId())
            .dni(user.getDni())
            .name(user.getName())
            .secondName(user.getSecondName())
            .permissions(user.getPermissionStrings())
            .token(jwtService.getToken(user))
            .build() : null;
    }

    @Override
    public List<UserSafeDto> getAllUsers() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        return users.stream().map(user -> user.toSafeDto()).toList();
    }

    @Override
    public LoginResponse update(UpdateUserDto updatedUserDto) {
        UserEntity userEntity = userRepository.findById(updatedUserDto.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity userFromToken = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken()))
            .orElseThrow(() -> new RuntimeException("Invalid token"));

        boolean isSameUser = userEntity.getId() == userFromToken.getId();
        boolean hasManageUsersPermission = userFromToken.getPermissionStrings()
            .contains(Permissions.manage_users.name());
        boolean isSuperAdmin = userFromToken.getPermissionStrings()
            .contains(Permissions.super_admin.name());

        if (isSameUser || hasManageUsersPermission || isSuperAdmin) {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEntity.getDni(), updatedUserDto.getPassword())
            );

            userEntity.setDni(updatedUserDto.getDni());
            userEntity.setName(updatedUserDto.getName());
            userEntity.setSecondName(updatedUserDto.getSecondName());
            userEntity.setDisabled(updatedUserDto.isDisabled());
            userEntity.setPermissions(updatedUserDto.getPermissions());
            userEntity.setGroups(updatedUserDto.getGroups());
            if ((isSameUser || isSuperAdmin) && updatedUserDto.getNewPassword() != null 
                && !updatedUserDto.getNewPassword().isEmpty()) {
                userEntity.setPassword(passwordEncoder.encode(updatedUserDto.getNewPassword()));
            }
            UserEntity savedUser = userRepository.save(userEntity);
            return isSameUser ? generateLoginResponse(savedUser) : null;
            // Send ws message to update user modifyed
        }

        throw new RuntimeException("Unauthorized to update user");
    }

    @Override
    public UserStatsEntity getUserStats(int userId) {
        return userStatsRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDetailedDto getDetailedUserById(int id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return user.toDetailedDto();
    }

    @Override
    public void toggleDisableUser(int id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDisabled(!user.isDisabled());
        userRepository.save(user);
        // Send ws message to update user modifyed
    }

    @Override
    public AvailableUserPermissionsDto getAvailableUserPermissions() {
        UserEntity userFromToken = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken()))
            .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (userFromToken.getPermissionStrings().contains(Permissions.super_admin.name())) {
            Set<PermissionEntity> allPermissions = StreamSupport
                .stream(permissionRepository.findAll().spliterator(), false)
                .filter(permission -> !permission.getName().equals("dev"))
                .collect(Collectors.toSet());

            Set<GroupEntity> allGroups = StreamSupport
                .stream(groupRepository.findAll().spliterator(), false)
                .filter(group -> !group.getName().equals("Dev"))
                .collect(Collectors.toSet());

            return AvailableUserPermissionsDto.builder()
                .permissions(allPermissions)
                .groups(allGroups)
                .build();
        }
        if (userFromToken.getPermissionStrings().contains(Permissions.dev.name())) {
            Set<PermissionEntity> allPermissions = StreamSupport
                .stream(permissionRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());

            Set<GroupEntity> allGroups = StreamSupport
                .stream(groupRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());

            return AvailableUserPermissionsDto.builder()
                .permissions(allPermissions)
                .groups(allGroups)
                .build();
        }
        return AvailableUserPermissionsDto.builder()
            .permissions(userFromToken.getPermissions())
            .groups(userFromToken.getGroups())
            .build();
    }

    @Override
    public GroupEntity save(GroupEntity groupEntity) {
        System.out.println(groupEntity.getId() + " " + groupEntity.getName() + " " + groupEntity.getPermissions());
        return groupRepository.save(groupEntity);
    }

    @Override
    public void deleteGroup(int id) {
        GroupEntity group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        groupRepository.delete(group);
        // Send ws message to update user modifyed
    }
}
