package com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "`Group`")
public class GroupEntity {

    @Id
    private int id;
    @Column(name = "name", length = 20, nullable = false, unique = true)
    private String name;
    @ManyToMany
    @JoinTable(name = "grouppermission",
        joinColumns = @JoinColumn(name = "groupid"),
        inverseJoinColumns = @JoinColumn(name = "permissionid"))
    private Set<PermissionEntity> permissions;
}
