package com.muffinmanager.api.muffinmanagerapi.controller.GenericController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.websocket.server.PathParam;

public interface GenericVersionController<TDto> extends GenericController<TDto> {
    @GetMapping("/getObsoletes")
    public ResponseEntity<List<TDto>> getObsoletes();

    @DeleteMapping("/delete/{reference}")
    public ResponseEntity<Void> deleteByReference(@PathVariable String reference);

    /*@PostMapping("/changeReference")
    public ResponseEntity<List<TDto>> changeReference(@PathParam("oldReference") String oldReference, @PathParam("newReference") String newReference);
    */
    
    @PostMapping("/setObsolete/{reference}")
    public ResponseEntity<Void> setObsoleteByReference(@PathVariable String reference, @PathParam("obsolete") boolean obsolete);

    @PostMapping("/setObsoleteById/{id}")
    public ResponseEntity<Void> setObsoleteById(@PathVariable int id, @PathParam("obsolete") boolean obsolete);
}
