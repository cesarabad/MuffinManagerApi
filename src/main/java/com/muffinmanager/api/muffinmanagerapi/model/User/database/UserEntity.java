package com.muffinmanager.api.muffinmanagerapi.model.User.database;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.GroupEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.PermissionEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "User")
public class UserEntity implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "dni", length = 9, nullable = false, unique = true)
    private String dni;
    
    @Column(name = "name", length = 80, nullable = false)
    private String name;
    
    @Column(name = "secondname", length = 80)
    private String secondName;
    
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "isdisabled", nullable = false)
    private boolean isDisabled;
    
    @ManyToMany
    @JoinTable(name = "usergroup",
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "groupid"))
    private Set<GroupEntity> groups;
    
    @ManyToMany
    @JoinTable(name = "userpermission",
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "permissionid"))
    private Set<PermissionEntity> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(
                    permissions.stream().map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getName())), 
                    groups.stream()
                                .flatMap(group -> group.getPermissions().stream())
                                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getName()))
            )
            .collect(Collectors.toSet());
    }
    @Override
    public String getUsername() {
        return this.dni;
    }

    public Set<String> getPermissionStrings() {
        return Stream.concat(
                    permissions.stream().map(permission -> permission.getName()), 
                    groups.stream()
                                .flatMap(group -> group.getPermissions().stream())
                                .map(permission -> permission.getName())
            )
            .collect(Collectors.toSet());
    }

    public UserSafeDto toSafeDto() {
        return UserSafeDto.builder()
            .id(id)
            .dni(dni)
            .name(name)
            .secondName(secondName)
            .build();
    }
}