package com.muffinmanager.api.muffinmanagerapi.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {

    public String getToken(UserDetails user);
    public String getDniFromToken(String token);
    public boolean isTokenValid(String token, UserDetails user);
}
