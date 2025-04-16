package com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.dto;

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
public class ProductItemDto {
    private int id;
    private String reference;
    private Integer baseProductItemId;
    private Integer brandId;
    private String mainDescription;
    private String ean13;
    private int version;
    private String aliasVersion;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private boolean isObsolete;
    private LocalDateTime lastModifyDate;
    private UserSafeDto lastModifyUser;
}
