package com.muffinmanager.api.muffinmanagerapi.service.GenericServiceInterface;

import java.util.List;

public interface IGenericCRUDVersionService<TDto> {
    public List<TDto> getNotObsoletesDetailed();
    public List<TDto> getNotObsoletes();
    public List<TDto> getObsoletes();
    public List<TDto> getAllInactives();
    public TDto save(TDto entityDto);
    public void deletePermanentlyByReference(String reference);
    public void deletePermanentlyByReferenceAndVersion(String reference, int version);
    public List<TDto> changeReference(String oldReference, String newReference);
    public void setObsoleteByReference(String reference, boolean obsolete);
    public void setObsoleteByReferenceAndVersion(String reference, int version, boolean obsolete);
    public void setActiveByReference(String reference, boolean active);
    public void setActiveByReferenceAndVersion(String reference, int version, boolean active);
}
