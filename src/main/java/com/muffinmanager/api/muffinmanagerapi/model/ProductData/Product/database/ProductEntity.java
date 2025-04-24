package com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.Box.Database.BoxEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto.ProductDto;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto.ProductLightDto;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.database.ProductItemEntity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "productreference", length = 20, nullable = false)
    private String productReference;
    @ManyToOne
    @JoinColumn(name = "boxid", nullable = false)
    private BoxEntity box;
    @ManyToOne
    @JoinColumn(name = "productitemid", nullable = false)
    private ProductItemEntity productItem;

    @Column(name = "itemsperproduct")
    private Integer itemsPerProduct;

    @Column(name = "ean14", length = 14)
    private String ean14;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "aliasversion", length = 50)
    private String aliasVersion;

    @Column(name = "creationdate", nullable = false)
    private Timestamp creationDate;

    @Column(name = "enddate")
    private Timestamp endDate;

    @Column(name = "isobsolete", nullable = false)
    private boolean isObsolete;

    @Column(name = "lastmodifydate", nullable = false)
    private Timestamp lastModifyDate;
    @ManyToOne
    @JoinColumn(name = "lastmodifyuserid")
    private UserEntity lastModifyUser;

    public ProductDto toDto() {
        return ProductDto.builder()
            .id(id)
            .reference(productReference)
            .boxId(box != null ? box.getId() : null)
            .boxInfo(box != null 
                ? box.getBoxReference() + " - " + box.getDescription() 
                : null)
            .productItemId(productItem != null ? productItem.getId() : null)
            .productItemInfo(productItem != null 
                ? productItem.getItemReference() + " - " 
                  + productItem.getBaseProductItem().getMuffinShape().getDescription()
                  + (productItem.getBaseProductItem().getMainDescription() != null 
                    ? " " + productItem.getBaseProductItem().getMainDescription() 
                    : "") + " "
                  + productItem.getBaseProductItem().getUnitsPerItem() + " UDS "
                  + productItem.getBrand().getName()
                : null)
            .itemsPerProduct(itemsPerProduct)
            .ean14(ean14)
            .version(version)
            .aliasVersion(aliasVersion)
            .creationDate(creationDate != null 
                ? creationDate.toLocalDateTime() 
                : null)
            .endDate(endDate != null 
                ? endDate.toLocalDateTime() 
                : null)
            .isObsolete(isObsolete)
            .lastModifyDate(lastModifyDate != null 
                ? lastModifyDate.toLocalDateTime() 
                : null)
            .lastModifyUser(lastModifyUser != null 
                ? lastModifyUser.toSafeDto() 
                : null)
            .build();
    }

    public ProductLightDto toLightDto() {
        return ProductLightDto.builder()
            .id(id)
            .reference(productReference)
            .description(productItem != null 
            ? 
              productItem.getBaseProductItem().getMuffinShape().getDescription()
              + (productItem.getBaseProductItem().getMainDescription() != null && !productItem.getBaseProductItem().getMainDescription().isEmpty() 
                ? " " + productItem.getBaseProductItem().getMainDescription()
                : "") + " "
              + productItem.getBaseProductItem().getUnitsPerItem() + " UDS "
              + productItem.getBrand().getName()
            : null)
            .boxReference(box != null 
            ? box.getBoxReference() 
            : null)
            .boxDescription(box != null
            ? box.getDescription()
            : null)
            .itemsPerProduct(itemsPerProduct)
            .aliasVersion(aliasVersion)
            .build();
    }
   
}
