package com.muffinmanager.api.muffinmanagerapi.model.User.dto;

import java.util.Set;

import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.GroupEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.PermissionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableUserPermissionsDto {
    private Set<PermissionEntity> permissions;
    private Set<GroupEntity> groups;
}
