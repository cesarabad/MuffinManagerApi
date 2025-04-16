package com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface;

import java.util.List;

public interface IGenericCRUDVersionService<TDto, TEntity> extends IGenericCRUDService<TDto, TEntity> {
    public List<TDto> getObsoletes();
    public void deleteByReference(String reference);
    //public List<TDto> changeReference(String oldReference, String newReference);
    public void setObsoleteByReference(String reference, boolean obsolete);
    public void setObsoleteById(int id, boolean obsolete);
}
