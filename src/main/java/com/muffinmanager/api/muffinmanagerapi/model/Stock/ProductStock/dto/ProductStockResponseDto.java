package com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.dto.PackagePrintLightDto;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto.ProductLightDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.ActiveReserveStockDto;

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
public class ProductStockResponseDto {
    private int id;
    private ProductLightDto product;
    private PackagePrintLightDto packagePrint;
    private String batch;
    private int stock;
    private String observations;
    private LocalDateTime lastCheckDate;
    private List<ActiveReserveStockDto> reserves;
}
