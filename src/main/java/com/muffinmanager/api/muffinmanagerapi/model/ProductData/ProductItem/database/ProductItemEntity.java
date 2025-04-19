package com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.Brand.database.BrandEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.database.BaseProductItemEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.dto.ProductItemDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.database.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "productitem")
public class ProductItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "itemreference", length = 20, nullable = false)
    private String itemReference;
    @JoinColumn(name = "baseproductitemid")
    @ManyToOne
    private BaseProductItemEntity baseProductItem;
    @JoinColumn(name = "brandid")
    @ManyToOne
    private BrandEntity brand;
    @Column(name = "ean13", length = 13)
    private String ean13;
    @Column(name="version", nullable = false)
    private int version;
    @Column(name = "aliasversion", length = 50)
    private String aliasVersion;
    @Column(name = "creationdate", nullable = false)
    private Timestamp creationDate;
    @Column(name = "enddate")
    private Timestamp endDate;
    @Column(name = "isobsolete", nullable = false)
    private Boolean isObsolete;
    @Column(name = "lastmodifydate", nullable = false)
    private Timestamp lastModifyDate;
    @JoinColumn(name = "lastmodifyuserid")
    @ManyToOne
    private UserEntity lastModifyUser;

    public ProductItemDto toDto() {
        return ProductItemDto.builder()
            .id(id)
            .reference(itemReference)
            .baseProductItemId(baseProductItem != null ? baseProductItem.getId() : null)
            .productItemInfo(getItemReference() + " - " + 
                getBaseProductItem().getMuffinShape().getDescription() + " "
                + (getBaseProductItem().getMainDescription() != null 
                    ? getBaseProductItem().getMainDescription() + " " : "")
                + getBaseProductItem().getUnitsPerItem() + " UDS "
                + (getBrand() != null ? getBrand().getName() : ""))
            .brandId(brand != null ? brand.getId() : null)
            .ean13(ean13)
            .version(version)
            .aliasVersion(aliasVersion)
            .creationDate(creationDate != null ? creationDate.toLocalDateTime() : null)
            .endDate(endDate != null ? endDate.toLocalDateTime() : null)
            .isObsolete(isObsolete)
            .lastModifyDate(lastModifyDate != null ? lastModifyDate.toLocalDateTime() : null)
            .lastModifyUser(lastModifyUser != null ? lastModifyUser.toSafeDto() : null)
            .build();
    }

    

    public ProductItemEntity clone() {
        return ProductItemEntity.builder()
            .itemReference(this.itemReference)
            .baseProductItem(this.baseProductItem)
            .brand(this.brand)
            .ean13(this.ean13)
            .version(this.version)
            .aliasVersion(this.aliasVersion)
            .creationDate(this.creationDate)
            .endDate(this.endDate)
            .isObsolete(this.isObsolete)
            .lastModifyDate(this.lastModifyDate)
            .lastModifyUser(this.lastModifyUser)
            .build();
    }
}
