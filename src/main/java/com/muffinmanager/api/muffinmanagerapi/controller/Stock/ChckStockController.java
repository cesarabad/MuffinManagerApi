package com.muffinmanager.api.muffinmanagerapi.controller.Stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.database.CheckStockEntity;
import com.muffinmanager.api.muffinmanagerapi.service.Stock.CheckStock.ICheckStockService;

@RestController
@RequestMapping(ChckStockController.BASE_URL)
public class ChckStockController {
    public static final String BASE_URL = "/stock/check-stock";
    
    @Autowired
    private ICheckStockService checkStockService;

    @PostMapping("create")
    public ResponseEntity<CheckStockEntity> create() {
        return ResponseEntity.ok(checkStockService.create());
    }

    @PostMapping("forceCreate")
    public ResponseEntity<CheckStockEntity> forceCreate() {
        return ResponseEntity.ok(checkStockService.forceCreate());
    }

    @PostMapping("cancel")
    public ResponseEntity<Void> cancel() {
        checkStockService.cancel();
        return ResponseEntity.ok().build();
    }

}
