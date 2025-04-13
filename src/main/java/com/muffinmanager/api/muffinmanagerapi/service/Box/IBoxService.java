package com.muffinmanager.api.muffinmanagerapi.service.Box;

import com.muffinmanager.api.muffinmanagerapi.model.Box.Database.BoxEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Box.dto.BoxDto;
import com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface.IGenericCRUDService;

public interface IBoxService extends IGenericCRUDService<BoxDto, BoxEntity> {
    
}
