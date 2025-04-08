package com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto.MuffinShapeDto;
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
@Table(name = "muffinshape")
public class MuffinShapeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "shapereference", length = 10, nullable = false)
    private String shapeReference;
    @Column(name = "description", length = 80, nullable = false)
    private String description;
    @Column(name = "lastmodifydate", nullable = false)
    private Timestamp lastModifyDate;
    @ManyToOne
    @JoinColumn(name = "lastmodifyuserid")
    private UserEntity lastModifyUser;


    public MuffinShapeDto toDto() {
        return new MuffinShapeDto(
            id, 
            shapeReference, 
            description,
            lastModifyDate != null ? 
                lastModifyDate.toLocalDateTime() : 
                null,
            lastModifyUser != null ?
                lastModifyUser.toSafeDto() : 
                null);
    }

    public MuffinShapeEntity clone() {
        return MuffinShapeEntity.builder()
            .shapeReference(this.shapeReference)
            .description(this.description)
            .lastModifyDate(this.lastModifyDate != null ? this.lastModifyDate : null)
            .lastModifyUser(this.lastModifyUser != null ? this.lastModifyUser : null)
            .build();
    }
}
