package com.muffinmanager.api.muffinmanagerapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.muffinmanager.api.muffinmanagerapi.model.User.database.stats.UserStatsEntity;

public interface IUserStatsRepository extends CrudRepository<UserStatsEntity, Integer> {
    
}
