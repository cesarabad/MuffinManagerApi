package com.muffinmanager.api.muffinmanagerapi.service.Stock.CheckStock;

import java.time.LocalDateTime;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.database.CheckStockEntity;

public interface ICheckStockService {
    // Al completarlo envia por websocket para informar al que lo hace
    public void verify();
    // Si existe envia por websocket para abrir modal y crear uno nuevo o cancelar y continuar con el existente
    public CheckStockEntity create();
    public CheckStockEntity forceCreate();
    public CheckStockEntity cancel();
    public boolean hasToCheckStock(LocalDateTime lastCheckDate);
}
