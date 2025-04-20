package com.muffinmanager.api.muffinmanagerapi.controller.Stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.MovementStockDto;
import com.muffinmanager.api.muffinmanagerapi.service.Stock.MovementStock.IMovementStockService;

@RestController
@RequestMapping(MovementStockController.BASE_URL)
public class MovementStockController {

    public static final String BASE_URL = "/stock/movement-stock";

    @Autowired
    private IMovementStockService movementStockService;

    @PostMapping("insert")
    public ResponseEntity<MovementStockDto> insert(@RequestBody MovementStockDto movementStockDto) {
        return ResponseEntity.ok(movementStockService.insert(movementStockDto));
    }

    @PostMapping("undoMovement/{movementStockId}")
    public MovementStockDto undoMovement(@PathVariable int movementStockId) {
        return movementStockService.undoMovement(movementStockId);
    }

    @PostMapping("endReserve/{movementStockId}")
    public MovementStockDto endReserve(@PathVariable int movementStockId) {
        return movementStockService.endReserve(movementStockId);
    }

    @GetMapping("getHistoric")
    public List<MovementStockDto> getHistoric() {
        return movementStockService.getHistoric();
    }

    @GetMapping("getHistoricByProductStockId/{productStockId}")
    public List<MovementStockDto> getHistoricByProductStockId(@PathVariable int productStockId) {
        return movementStockService.getHistoricByProductStockId(productStockId);
    }

    @GetMapping("getHistoricByProductId/{productId}")
    public List<MovementStockDto> getHistoricByProductId(@PathVariable int productId) {
        return movementStockService.getHistoricByProductId(productId);
    }
}
