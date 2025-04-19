package com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.database;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.database.PackagePrintEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.database.ProductEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.database.MovementStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockDto;

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
@FilterDef(name = "onlyReserves", defaultCondition = "type = 4 AND status = 1")
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

    @Filter(name = "onlyReserves")
    @OneToMany(mappedBy = "productStock")
    private List<MovementStockEntity> reserves;

    public ProductStockDto toDto() {
        return ProductStockDto.builder()
            .id(id)
            .productId(product != null ? product.getId() : 0)
            .packagePrintId(packagePrint != null ? packagePrint.getId() : 0)
            .batch(batch)
            .stock(stock)
            .observations(observations)
            .lastCheckDate(lastCheckDate != null ? lastCheckDate.toLocalDateTime() : null)
            .reserves(reserves != null ? reserves.stream().map(MovementStockEntity::toDto).toList() : null)
            .build();
    }
}

