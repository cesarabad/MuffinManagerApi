package com.muffinmanager.api.muffinmanagerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto.MuffinShapeDto;
import com.muffinmanager.api.muffinmanagerapi.service.MuffinShape.IMuffinShapeService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/manage/muffin-shape")
public class MuffinShapeController {

    @Autowired
    private IMuffinShapeService muffinShapeService;

    @GetMapping("/notobsoletes-detailed")
    public ResponseEntity<List<MuffinShapeDto>> getNotObsoletesDetailed() {
        return ResponseEntity.ok(muffinShapeService.getNotObsoletesDetailed());
    }

    @GetMapping("/notobsoletes")
    public ResponseEntity<List<MuffinShapeDto>> getNotObsoletes() {
        return ResponseEntity.ok(muffinShapeService.getNotObsoletes());
    }
    
    @GetMapping("/obsoletes")
    public ResponseEntity<List<MuffinShapeDto>> getObsoletes() {
        return ResponseEntity.ok(muffinShapeService.getObsoletes());
    }

    @GetMapping("/all/{reference}") 
    public ResponseEntity<List<MuffinShapeDto>> getAllVersionsByReference(@PathVariable String reference) {
        return ResponseEntity.ok(muffinShapeService.getAllVersionsByReference(reference));
    }

    @PatchMapping("/setObsolete/all/{reference}")
    public ResponseEntity<Boolean> setObsoleteByReference(@PathVariable String reference, @PathParam("obsolete") boolean obsolete) {
        muffinShapeService.setObsoleteByReference(reference, obsolete);
        return ResponseEntity.ok(true);
    }

    @PatchMapping("/setObsolete/{reference}")
    public ResponseEntity<Boolean> setObsoleteByReferenceAndVersion(@PathVariable String reference, @PathParam("version") int version, @PathParam("obsolete") boolean obsolete) {
        muffinShapeService.setObsoleteByReferenceAndVersion(reference, version, obsolete);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/save")
    public MuffinShapeDto saveMuffinShape(@RequestBody MuffinShapeDto entity) {
        return muffinShapeService.save(entity);
    }

    @PatchMapping("/setActive/all/{reference}")
    public ResponseEntity<Boolean> setActiveByReference(@PathVariable String reference, @PathParam("isActive") boolean isActive) {
        muffinShapeService.setActiveByReference(reference, isActive);
        return ResponseEntity.ok(true);
    }

    @PatchMapping("/setActive/{reference}")
    public ResponseEntity<Boolean> deleteByReferenceAndVersion(@PathVariable String reference, @PathParam("version") int version, @PathParam("isActive") boolean isActive) {
        muffinShapeService.setActiveByReferenceAndVersion(reference, version, isActive);
        return ResponseEntity.ok(true);
    }

    @PatchMapping("/changeReference")
    public ResponseEntity<List<MuffinShapeDto>> changeReference(@PathParam("oldReference") String oldReference, @PathParam("newReference") String newReference) {
        return ResponseEntity.ok(muffinShapeService.changeReference(oldReference, newReference));
    }
}
