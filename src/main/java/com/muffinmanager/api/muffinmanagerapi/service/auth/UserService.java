package com.muffinmanager.api.muffinmanagerapi.service.auth;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginResponse;
import com.muffinmanager.api.muffinmanagerapi.model.User.RegisterRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.UserEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;

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
            .groups(new HashSet<>())
            .permissions(new HashSet<>())
            .build();
        userRepository.save(user);
        return generateLoginResponse(user);
    }

    private LoginResponse generateLoginResponse(UserEntity user) {
        return LoginResponse.builder()
            .id(user.getId())
            .dni(user.getDni())
            .name(user.getName())
            .secondName(user.getSecondName())
            .permissions(user.getPermissionStrings())
            .token(jwtService.getToken(user))
            .build();
    }

    @Override
    public List<UserSafeDto> getAllUsers() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();
        return users.stream().map(user -> UserSafeDto.builder()
            .id(user.getId())
            .dni(user.getDni())
            .name(user.getName())
            .secondName(user.getSecondName())
            .build()).toList();
    }
    

}
