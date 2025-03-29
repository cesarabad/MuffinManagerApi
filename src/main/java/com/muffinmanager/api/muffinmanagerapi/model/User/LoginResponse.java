package com.muffinmanager.api.muffinmanagerapi.model.User;

import java.util.Collection;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String dni;
    private String name;
    private String secondName;
    private Collection<String> permissions;
    private String token;
}
