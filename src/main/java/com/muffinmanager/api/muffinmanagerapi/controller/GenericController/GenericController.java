package com.muffinmanager.api.muffinmanagerapi.controller.GenericController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GenericController <TDto> {
    @GetMapping("/getAll")
    public ResponseEntity<List<TDto>> getAll() ;

    @GetMapping("/getById/{id}")
    public ResponseEntity<TDto> getById(@PathVariable int id) ;

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) ;

    @PostMapping("/insert")
    public ResponseEntity<TDto> insert(@RequestBody TDto entityDto) ;

    @PostMapping("/update")
    public ResponseEntity<TDto> update(@RequestBody TDto entityDto) ;
}
