package com.muffinmanager.api.muffinmanagerapi.service.Stock.ProductStock;

import java.util.List;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockRequestDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockResponseDto;

public interface IProductStockService {
    public Object getAllGroupedByBrandThenMuffinShapeThenProductId();
    public List<ProductStockResponseDto> getAllByProductId(int productId);
    public ProductStockResponseDto insert(ProductStockRequestDto productStockDto);
    public ProductStockResponseDto update(ProductStockRequestDto productStockDto);
    public void deleteById(int id);
    public void updateLastUpdateDate(int id);
}
