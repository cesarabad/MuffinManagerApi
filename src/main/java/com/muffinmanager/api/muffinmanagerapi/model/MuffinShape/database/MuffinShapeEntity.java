package com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto.MuffinShapeDetailedDto;
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto.MuffinShapeListDto;
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
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "startdate", nullable = false)
    private Timestamp startDate;
    @Column(name = "enddate")
    private Timestamp endDate;
    @Column(name = "isactive", nullable = false)
    private boolean isActive;
    @Column(name = "isobsolete", nullable = false)
    private boolean isObsolete;
    @ManyToOne
    @JoinColumn(name = "lastmodifyuser")
    private UserEntity lastModifyUser;

    public MuffinShapeDetailedDto toDetaliedDto() {
        return new MuffinShapeDetailedDto( 
            shapeReference, 
            description, 
            version,
            id,
            startDate.toLocalDateTime(), 
            endDate != null ?
                endDate.toLocalDateTime() : 
                null,
            isObsolete, 
            lastModifyUser != null ?
                lastModifyUser.toSafeDto() : 
                null);
    }

    public MuffinShapeListDto toListDto() {
        return new MuffinShapeListDto(
            shapeReference, 
            description,
            version,
            id, 
            isObsolete, 
            lastModifyUser != null ?
                lastModifyUser.toSafeDto() : 
                null);
    }

    public MuffinShapeEntity clone() {
        return MuffinShapeEntity.builder()
            .shapeReference(this.shapeReference)
            .description(this.description)
            .version(this.version)
            .startDate(this.startDate)
            .endDate(this.endDate)
            .isActive(this.isActive)
            .isObsolete(this.isObsolete)
            .lastModifyUser(this.lastModifyUser != null ? this.lastModifyUser : null)
            .build();
    }
}
