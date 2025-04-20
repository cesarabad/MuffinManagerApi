package com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.ActiveReserveStockDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.MovementStockDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.enums.MovementStatus;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.enums.MovementType;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.database.ProductStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "movementstock")
public class MovementStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "productstockid")
    private ProductStockEntity productStock;
    @Enumerated(EnumType.STRING)
    private MovementType type;
    @ManyToOne
    @JoinColumn(name = "responsibleuserid")
    private UserEntity responsible;
    @Column(name = "stockdifference")
    private int stockDifference;
    private int units;
    private String destination;
    @Column(name = "creationdate", nullable = false)
    private Timestamp creationDate;
    @Column(name = "enddate")
    private Timestamp endDate;
    private String observations;
    @Enumerated(EnumType.STRING)
    private MovementStatus status;

    public MovementStockDto toDto() {
        return MovementStockDto.builder()
            .id(id)
            .productStockId(productStock != null ? productStock.getId() : 0)
            .productReference(productStock != null ? productStock.getProduct().getProductReference() : null)
            .batch(productStock != null ? productStock.getBatch() : null)
            .packagePrintDescription(productStock != null ? productStock.getPackagePrint().getDescription() : null)
            .type(type)
            .responsible(responsible != null ? responsible.toSafeDto() : null)
            .units(units)
            .destination(destination)
            .creationDate(creationDate != null ? creationDate.toLocalDateTime() : null)
            .endDate(endDate != null ? endDate.toLocalDateTime() : null)
            .observations(observations)
            .status(status)
            .build();
    }

    public ActiveReserveStockDto toActiveReserveDto() {
        return ActiveReserveStockDto.builder()
            .id(id)
            .responsible(responsible != null ? responsible.toSafeDto() : null)
            .units(units)
            .destination(destination)
            .creationDate(creationDate != null ? creationDate.toLocalDateTime() : null)
            .observations(observations)
            .build();
    }
}
