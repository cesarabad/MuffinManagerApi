package com.muffinmanager.api.muffinmanagerapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.database.BaseProductItemEntity;

@Repository
public interface IBaseProductItemRepository extends CrudRepository<BaseProductItemEntity, Integer>{
    
}
