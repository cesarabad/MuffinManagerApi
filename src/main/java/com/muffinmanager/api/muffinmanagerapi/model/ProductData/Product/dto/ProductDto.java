package com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto;

import java.time.LocalDateTime;

import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

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
public class ProductDto {
    private int id;
    private String reference;
    private Integer boxId;
    private Integer productItemId;
    private Integer itemsPerProduct;
    private String ean14;
    private int version;
    private String aliasVersion;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private boolean isObsolete;
    private LocalDateTime lastModifyDate;
    private UserSafeDto lastModifyUser;
}
