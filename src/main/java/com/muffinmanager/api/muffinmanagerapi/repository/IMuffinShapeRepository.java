package com.muffinmanager.api.muffinmanagerapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.database.MuffinShapeEntity;

@Repository
public interface IMuffinShapeRepository extends CrudRepository<MuffinShapeEntity, Integer>{

}
