package com.muffinmanager.api.muffinmanagerapi.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String dni;
    private String name;
    private String secondName;
    private String password;
}
