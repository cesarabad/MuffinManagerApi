package com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto;

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
public class ProductLightDto {
    private int id;
    private String reference;
    private String description;
    private String boxReference;
    private String boxDescription;
    private Integer itemsPerProduct;
    private String aliasVersion;
}
