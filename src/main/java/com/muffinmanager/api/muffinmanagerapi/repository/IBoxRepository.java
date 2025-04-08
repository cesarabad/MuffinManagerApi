package com.muffinmanager.api.muffinmanagerapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.Box.Database.BoxEntity;

@Repository
public interface IBoxRepository extends CrudRepository<BoxEntity, Integer> {

    
}
