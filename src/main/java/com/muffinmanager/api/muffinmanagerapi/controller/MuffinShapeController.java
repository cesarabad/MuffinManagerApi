package com.muffinmanager.api.muffinmanagerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.controller.GenericController.GenericController;
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto.MuffinShapeDto;
import com.muffinmanager.api.muffinmanagerapi.service.MuffinShape.IMuffinShapeService;

@RestController
@RequestMapping("/manage/muffin-shape")
public class MuffinShapeController implements GenericController<MuffinShapeDto> {

    @Autowired
    private IMuffinShapeService muffinShapeService;

    @Override
    public ResponseEntity<List<MuffinShapeDto>> getAll() {
        return ResponseEntity.ok(muffinShapeService.getAll());
    }

    @Override
    public ResponseEntity<MuffinShapeDto> getById(int id) {
        return ResponseEntity.ok(muffinShapeService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        muffinShapeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<MuffinShapeDto> insert(MuffinShapeDto entityDto) {
        return ResponseEntity.ok(muffinShapeService.insert(entityDto));
    }

    @Override
    public ResponseEntity<MuffinShapeDto> update(MuffinShapeDto entityDto) {
        return ResponseEntity.ok(muffinShapeService.update(entityDto));
    }
    
}
