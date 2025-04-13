package com.muffinmanager.api.muffinmanagerapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.database.PackagePrintEntity;

@Repository
public interface IPackagePrintRepository extends CrudRepository<PackagePrintEntity, Integer> {
    
}
