package com.muffinmanager.api.muffinmanagerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.database.MuffinShapeEntity;

@Repository
public interface IMuffinShapeRepository extends CrudRepository<MuffinShapeEntity, Integer>{
    public abstract Optional<List<MuffinShapeEntity>> findByShapeReference(String shapeReference);
    public abstract Optional<MuffinShapeEntity> findByShapeReferenceAndVersion(String shapeReference, int version);
    @Query("SELECT MAX(m.version) FROM MuffinShapeEntity m WHERE m.shapeReference = :shapeReference")
    public abstract Optional<Integer> findMaxVersionByShapeReference(@Param("shapeReference") String shapeReference);
}
