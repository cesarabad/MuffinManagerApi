package com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto;


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
public class MovementStockDto {
    private int id;
    private int productStockId;
    private int type;
    private UserSafeDto responsible;
    private int units;
    private String destination;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private String observations;
    private int status;
}
