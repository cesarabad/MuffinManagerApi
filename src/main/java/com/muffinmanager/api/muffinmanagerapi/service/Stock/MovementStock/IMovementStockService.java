package com.muffinmanager.api.muffinmanagerapi.service.Stock.MovementStock;

import java.util.List;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.MovementStockDto;

public interface IMovementStockService {
    public MovementStockDto insert(MovementStockDto movementStockDto);
    public List<MovementStockDto> getHistoric();
    public List<MovementStockDto> getHistoricByProductId(int productId);
    public List<MovementStockDto> getHistoricByProductStockId(int productStockId);
    public MovementStockDto undoMovement(int movementStockId);
    public MovementStockDto endReserve(int movementStockId);
}
