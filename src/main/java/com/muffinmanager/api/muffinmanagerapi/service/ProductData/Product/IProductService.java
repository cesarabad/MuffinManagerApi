package com.muffinmanager.api.muffinmanagerapi.service.ProductData.Product;

import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.database.ProductEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto.ProductDto;
import com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface.IGenericCRUDVersionService;

public interface IProductService extends IGenericCRUDVersionService<ProductDto, ProductEntity>{
    
}
