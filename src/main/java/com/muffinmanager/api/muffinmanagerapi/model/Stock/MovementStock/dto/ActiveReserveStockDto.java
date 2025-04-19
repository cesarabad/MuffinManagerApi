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
public class ActiveReserveStockDto {
    private int id;
    private UserSafeDto responsible;
    private int units;
    private String destination;
    private LocalDateTime creationDate;
    private String observations;
}
