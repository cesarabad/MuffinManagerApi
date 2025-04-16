package com.muffinmanager.api.muffinmanagerapi.service.ProductData.ProductItem;

import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.database.ProductItemEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.dto.ProductItemDto;
import com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface.IGenericCRUDVersionService;

public interface IProductItemService extends IGenericCRUDVersionService<ProductItemDto, ProductItemEntity> {
    
}
