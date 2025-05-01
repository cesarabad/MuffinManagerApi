package com.muffinmanager.api.muffinmanagerapi.model.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDto {
    private int totalMovements;
    private int totalReserves;
    private int totalStockChcecked;
}
