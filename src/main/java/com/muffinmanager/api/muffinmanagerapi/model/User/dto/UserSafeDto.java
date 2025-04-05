package com.muffinmanager.api.muffinmanagerapi.model.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSafeDto {
    private int id;
    private String dni;
    private String name;
    private String secondName;
}
