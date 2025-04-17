package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.database.ProductItemEntity;
@Repository
public interface IProductItemRepository extends CrudRepository<ProductItemEntity, Integer> {
    @Query("SELECT MAX(p.version) FROM ProductItemEntity p WHERE p.itemReference = :reference")
    Optional<Integer> findHighestVersionByReference(@Param("reference") String reference);
    Optional<List<ProductItemEntity>> findByItemReference(String reference);
    @Query("SELECT p FROM ProductItemEntity p WHERE p.brand.brandReference = :reference")
    Optional<List<ProductItemEntity>> findByBrandReference(@Param("reference") String reference);
}
