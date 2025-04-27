package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.database.CheckStockEntity;
@Repository
public interface ICheckStockRepository extends CrudRepository<CheckStockEntity, Integer> {
    @Query("SELECT c FROM CheckStockEntity c WHERE c.status = 'InProgress'")
    Optional<CheckStockEntity> findActiveCheckStock();
}
