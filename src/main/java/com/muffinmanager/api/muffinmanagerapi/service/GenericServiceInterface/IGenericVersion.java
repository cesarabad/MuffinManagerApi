package com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface;

import java.util.List;

public interface IGenericVersion<TDto> {
    public List<TDto> getNotObsoletesDetailed();
    public List<TDto> getNotObsoletes();
    public List<TDto> getObsoletes();
    public void setObsoleteByReference(String reference, boolean obsolete);
    public void setObsoleteByReferenceAndVersion(String reference, int version, boolean obsolete);
    public TDto save(TDto entityDto);
    public List<TDto> changeReference(String oldReference, String newReference);
    public List<TDto> getAllVersionsByReference(String reference);
    public void setActiveByReference(String reference, boolean active);
    public void setActiveByReferenceAndVersion(String reference, int version, boolean active);
}
