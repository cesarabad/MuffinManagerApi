package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.Brand.database.BrandEntity;

@Repository
public interface IBrandRepository extends CrudRepository<BrandEntity, Integer>{
    @Query("SELECT MAX(b.version) FROM BrandEntity b WHERE b.brandReference = :reference")
    Optional<Integer> findHighestVersionByReference(@Param("reference") String reference);
    Optional<List<BrandEntity>> findByBrandReference(String reference);
    Optional<BrandEntity> findByBrandReferenceAndVersion(String reference, int version);
}
