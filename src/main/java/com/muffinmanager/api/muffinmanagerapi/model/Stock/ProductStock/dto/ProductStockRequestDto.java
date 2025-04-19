package com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductStockRequestDto {
    private int id;
    private int productId;
    private int packagePrintId;
    private String batch;
    private int stock;
    private String observations;
    private LocalDateTime lastCheckDate;
}
