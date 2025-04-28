package com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.database;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.annotations.Where;

import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.database.PackagePrintEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.database.ProductEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.database.MovementStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockRequestDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productstock")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;

    @Column(name = "batch", length = 5, nullable = false)
    private String batch;

    @ManyToOne
    @JoinColumn(name = "packageprintid")
    private PackagePrintEntity packagePrint;

    @Column(name = "observations", length = 80)
    private String observations;

    @Column(name = "stock", nullable = false)   
    private int stock;

    @Column(name = "lastcheckdate")
    private Timestamp lastCheckDate;

    @Column(name = "isdeleted", nullable = false)
    private boolean isDeleted;

    @SuppressWarnings("deprecation")
    @OneToMany(mappedBy = "productStock")
    @Where(clause = "type = 'Reserve' AND status = 'InProgress'")
    private List<MovementStockEntity> reserves;

    public void setReserves(List<MovementStockEntity> reserves) {
        this.reserves = reserves;
    }

    public ProductStockRequestDto toRequestDto() {
        return ProductStockRequestDto.builder()
            .productId(product.getId())
            .packagePrintId(packagePrint != null ? packagePrint.getId() : 0)
            .batch(batch)
            .stock(stock)
            .observations(observations)
            .lastCheckDate(lastCheckDate != null ? lastCheckDate.toLocalDateTime() : null)
            .build();
    }

    public ProductStockResponseDto toResponseDto() {
        return ProductStockResponseDto.builder()
            .id(id)
            .packagePrint(packagePrint != null ? packagePrint.toLightDto() : null)
            .batch(batch)
            .stock(stock)
            .observations(observations)
            .lastCheckDate(lastCheckDate != null ? lastCheckDate.toLocalDateTime() : null)
            .hasToCheck(false)
            .reserves(reserves != null ? reserves.stream().map(MovementStockEntity::toActiveReserveDto).toList() : null)
            .build();
    }
}

