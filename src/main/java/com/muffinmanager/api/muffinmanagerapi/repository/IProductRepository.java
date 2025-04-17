package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.database.ProductEntity;

@Repository
public interface IProductRepository extends CrudRepository<ProductEntity, Integer> {
    @Query("SELECT MAX(p.version) FROM ProductEntity p WHERE p.productReference = :reference")
    Optional<Integer> findHighestVersionByReference(String reference);
    Optional<List<ProductEntity>> findByProductReference(String reference);
}
