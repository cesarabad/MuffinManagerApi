package com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.dto;

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
public class BaseProductItemDto {
    private int id;
    private String reference;
    private String mainDescription;
    private Integer muffinShape;
    private Float weight;
    private Integer unitsPerItem;
    private LocalDateTime lastModifyDate;
    private UserSafeDto lastModifyUser;
}
