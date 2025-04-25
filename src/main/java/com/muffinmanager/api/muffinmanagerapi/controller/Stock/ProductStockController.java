package com.muffinmanager.api.muffinmanagerapi.controller.Stock;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockRequestDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockResponseDto;
import com.muffinmanager.api.muffinmanagerapi.service.Stock.ProductStock.IProductStockService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping(ProductStockController.BASE_URL)
public class ProductStockController {
    
    public static final String BASE_URL = "/stock/product-stock";

    @Autowired
    private IProductStockService productStockService;

    @PostMapping("insert")
    public ResponseEntity<ProductStockResponseDto> insert(@RequestBody ProductStockRequestDto productStockDto) {
        return ResponseEntity.ok(productStockService.insert(productStockDto));
    }

    @GetMapping("getGroupedBy")
    public ResponseEntity<Object> getAllGroupedByBrandThenMuffinShapeThenProductId() {
        return ResponseEntity.ok(productStockService.getAllGroupedByBrandThenMuffinShapeThenProductId());
    }
    
    @GetMapping("getByProductId")
    public ResponseEntity<List<ProductStockResponseDto>> getAllByProductId(@PathParam("productId") int productId) {
        return ResponseEntity.ok(productStockService.getAllByProductId(productId));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        productStockService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("update")
    public ResponseEntity<ProductStockResponseDto> update(@RequestBody ProductStockRequestDto productStockDto) {
        return ResponseEntity.ok(productStockService.update(productStockDto));
    }

    @PostMapping("updateLastUpdateDate/{id}")
    public ResponseEntity<Void> updateLastUpdateDate(@PathVariable int id) {
        productStockService.updateLastUpdateDate(id);
        return ResponseEntity.ok().build();
    }
    
}
