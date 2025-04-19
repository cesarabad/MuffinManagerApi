package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.database.ProductStockEntity;

@Repository
public interface IProductStockRepository extends CrudRepository<ProductStockEntity, Integer>{
    Optional<List<ProductStockEntity>> findByProductId(int productId);
}
