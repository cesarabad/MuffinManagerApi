package com.muffinmanager.api.muffinmanagerapi.model.User;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private int id;
    private String dni;
    private String name;
    private String secondName;
    private Set<String> permissions;
    private String token;
}
