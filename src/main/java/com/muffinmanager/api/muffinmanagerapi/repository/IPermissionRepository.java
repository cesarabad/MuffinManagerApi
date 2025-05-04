package com.muffinmanager.api.muffinmanagerapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.User.database.permissions.PermissionEntity;

@Repository
public interface IPermissionRepository extends CrudRepository<PermissionEntity, Integer> {
    
}
