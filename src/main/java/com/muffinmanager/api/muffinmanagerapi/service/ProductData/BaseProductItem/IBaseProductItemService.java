package com.muffinmanager.api.muffinmanagerapi.service.ProductData.BaseProductItem;

import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.database.BaseProductItemEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.dto.BaseProductItemDto;
import com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface.IGenericCRUDService;

public interface IBaseProductItemService  extends IGenericCRUDService<BaseProductItemDto, BaseProductItemEntity> {
    
}
