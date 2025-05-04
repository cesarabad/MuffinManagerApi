package com.muffinmanager.api.muffinmanagerapi.model.User.dto;

import java.util.Set;

import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.GroupEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.PermissionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailedDto {
    private int id;
    private String dni;
    private String name;
    private String secondName;
    private boolean isDisabled;
    private Set<PermissionEntity> permissions;
    private Set<GroupEntity> groups;
}
