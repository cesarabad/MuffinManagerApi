package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.User.database.UserEntity;

@Repository
public interface IUserRepository extends CrudRepository<UserEntity, Integer> {
    @EntityGraph(attributePaths = {"groups.permissions", "permissions"})
    public abstract Optional<UserEntity> findByDni(String dni);
}
