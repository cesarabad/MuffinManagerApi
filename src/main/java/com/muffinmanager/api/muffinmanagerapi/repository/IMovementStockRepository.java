package com.muffinmanager.api.muffinmanagerapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.database.MovementStockEntity;

@Repository
public interface IMovementStockRepository extends CrudRepository<MovementStockEntity, Integer> {
    
}
