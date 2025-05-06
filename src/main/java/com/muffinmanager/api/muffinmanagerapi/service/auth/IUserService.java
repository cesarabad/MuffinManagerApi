package com.muffinmanager.api.muffinmanagerapi.service.auth;

import java.util.List;

import com.muffinmanager.api.muffinmanagerapi.model.User.LoginRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginResponse;
import com.muffinmanager.api.muffinmanagerapi.model.User.RegisterRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.GroupEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.stats.UserStatsEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.AvailableUserPermissionsDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UpdateUserDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserDetailedDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

public interface IUserService {
    public LoginResponse login(LoginRequest request);
    public LoginResponse refreshToken(String token);
    public LoginResponse register(RegisterRequest request);
    public List<UserSafeDto> getAllUsers();
    public LoginResponse update(UpdateUserDto userSafeDto);
    public UserStatsEntity getUserStats(int userId);
    public UserDetailedDto getDetailedUserById(int id);
    public void toggleDisableUser(int id);
    public AvailableUserPermissionsDto getAvailableUserPermissions();
    public GroupEntity save(GroupEntity groupEntity);
    public void deleteUser(int id);
    public void deleteGroup(int id);
}
