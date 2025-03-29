package com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Permission")
public class PermissionEntity {

    @Id
    private int id; 
    @Column(name = "name", length = 20, nullable = false, unique = true)
    private String name;
}
