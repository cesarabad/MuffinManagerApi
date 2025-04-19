package com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.MovementStockDto;

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
public class ProductStockDto {
    private int id;
    private int productId;
    private int packagePrintId;
    private String batch;
    private int stock;
    private String observations;
    private LocalDateTime lastCheckDate;
    private List<MovementStockDto> reserves;
}
