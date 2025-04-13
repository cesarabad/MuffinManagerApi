package com.muffinmanager.api.muffinmanagerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.controller.GenericController.GenericVersionController;
import com.muffinmanager.api.muffinmanagerapi.model.Brand.dto.BrandDto;
import com.muffinmanager.api.muffinmanagerapi.service.Brand.IBrandService;

@RestController
@RequestMapping(BrandController.BASE_URL)
public class BrandController implements GenericVersionController<BrandDto> {

    public static final String BASE_URL = "/manage/brand";

    @Autowired
    private IBrandService brandService;
    
    @Override
    public ResponseEntity<List<BrandDto>> getAll() {
        return ResponseEntity.ok(brandService.getAll());
    }

    @Override
    public ResponseEntity<BrandDto> getById(int id) {
        return ResponseEntity.ok(brandService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        brandService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BrandDto> insert(BrandDto entityDto) {
        return ResponseEntity.ok(brandService.insert(entityDto));
    }

    @Override
    public ResponseEntity<BrandDto> update(BrandDto entityDto) {
        return ResponseEntity.ok(brandService.update(entityDto));
    }

    @Override
    public ResponseEntity<List<BrandDto>> getObsoletes() {
        return ResponseEntity.ok(brandService.getObsoletes());
    }

    @Override
    public ResponseEntity<Void> deleteByReference(String reference) {
        brandService.deleteByReference(reference);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<BrandDto>> changeReference(String oldReference, String newReference) {
        return ResponseEntity.ok(brandService.changeReference(oldReference, newReference));
    }

    @Override
    public ResponseEntity<Void> setObsoleteByReference(String reference, boolean obsolete) {
        brandService.setObsoleteByReference(reference, obsolete);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setObsoleteById(int id, boolean obsolete) {
        brandService.setObsoleteById(id, obsolete);
        return ResponseEntity.ok().build();
    }
}
