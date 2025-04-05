package com.muffinmanager.api.muffinmanagerapi.service.auth;

import java.util.List;

import com.muffinmanager.api.muffinmanagerapi.model.User.LoginRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.LoginResponse;
import com.muffinmanager.api.muffinmanagerapi.model.User.RegisterRequest;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

public interface IUserService {
    public LoginResponse login(LoginRequest request);
    public LoginResponse register(RegisterRequest request);
    public List<UserSafeDto> getAllUsers();
}
