package com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.database.MuffinShapeEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.dto.BaseProductItemDto;
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
@Table(name = "baseproductitem")
public class BaseProductItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "baseitemreference", length = 20, nullable = false)
    private String baseItemReference;
    @Column(name = "maindescription", length = 80, nullable = false)
    private String mainDescription;
    @ManyToOne
    @JoinColumn(name = "muffinshapeid")
    private MuffinShapeEntity muffinShape;
    @Column(name = "weight", nullable = false)
    private Float weight;
    @Column(name = "unitsperitem")
    private Integer unitsPerItem;
    @Column(name = "lastmodifydate", nullable = false)
    private Timestamp lastModifyDate;
    @JoinColumn(name = "lastmodifyuserid")
    @ManyToOne
    private UserEntity lastModifyUser;

    public BaseProductItemDto toDto() {
        return BaseProductItemDto.builder()
            .id(id)
            .reference(baseItemReference)
            .mainDescription(mainDescription)
            .muffinShape(muffinShape != null ? muffinShape.getId() : null)
            .weight(weight)
            .unitsPerItem(unitsPerItem)
            .lastModifyDate(lastModifyDate != null ? lastModifyDate.toLocalDateTime() : null)
            .lastModifyUser(lastModifyUser != null ? lastModifyUser.toSafeDto() : null)
            .build();
    }
}
