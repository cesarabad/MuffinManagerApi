package com.muffinmanager.api.muffinmanagerapi.service.Brand;

import com.muffinmanager.api.muffinmanagerapi.model.Brand.database.BrandEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Brand.dto.BrandDto;
import com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface.IGenericCRUDVersionService;

public interface IBrandService extends IGenericCRUDVersionService<BrandDto, BrandEntity> {
    
}
