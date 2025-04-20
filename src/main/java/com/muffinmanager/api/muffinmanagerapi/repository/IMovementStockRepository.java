package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.database.MovementStockEntity;

@Repository
public interface IMovementStockRepository extends CrudRepository<MovementStockEntity, Integer> {
    @Query("SELECT m FROM MovementStockEntity m WHERE m.productStock.id = :productStockId")
    Optional<List<MovementStockEntity>> findByProductStockId(@Param("productStockId") int productStockId);
    
}
