package com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.enums.CheckStockStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "checkstock")
public class CheckStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private CheckStockStatus status;
    @Column(name = "creationdate", nullable = false)
    private Timestamp startDate;
    @Column(name = "enddate")
    private Timestamp endDate;
}
