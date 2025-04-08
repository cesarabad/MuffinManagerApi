package com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface;

import java.util.List;

public interface IGenericCRUDService<TDto, TEntity> {
    public List<TDto> getAll();
    public TDto getById(int id);
    public TDto insert(TDto entityDto);
    public TDto update(TDto entityDto) ;
    public void deleteById(int id) ;
    public TEntity getEntityByDto(TDto dto) ;
}
