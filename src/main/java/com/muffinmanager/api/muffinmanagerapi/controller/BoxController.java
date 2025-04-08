package com.muffinmanager.api.muffinmanagerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.controller.GenericController.GenericController;
import com.muffinmanager.api.muffinmanagerapi.model.Box.BoxDto;
import com.muffinmanager.api.muffinmanagerapi.service.Box.IBoxService;

@RestController
@RequestMapping("/manage/box")
public class BoxController implements GenericController<BoxDto> {
    
    @Autowired
    private IBoxService boxService;

    @Override
    public ResponseEntity<List<BoxDto>> getAll() {
        return ResponseEntity.ok(boxService.getAll());
    }

    @Override
    public ResponseEntity<BoxDto> getById(int id) {
        return ResponseEntity.ok(boxService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        boxService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BoxDto> insert(BoxDto entityDto) {
        return ResponseEntity.ok(boxService.insert(entityDto));
    }

    @Override
    public ResponseEntity<BoxDto> update(BoxDto entityDto) {
        return ResponseEntity.ok(boxService.update(entityDto));
    }


}
