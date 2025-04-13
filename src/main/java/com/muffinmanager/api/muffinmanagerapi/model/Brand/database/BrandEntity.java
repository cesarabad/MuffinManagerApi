package com.muffinmanager.api.muffinmanagerapi.model.Brand.database;

import java.sql.Timestamp;
import java.util.Base64;

import com.muffinmanager.api.muffinmanagerapi.model.Brand.dto.BrandDto;
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
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Getter
@Setter
@Table(name = "brand")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "brandreference", length = 10, nullable = false)
    private String brandReference;
    
    @Column(name = "name", length = 80, nullable = false)
    private String name;
   
    @Column(name = "logo", columnDefinition = "BLOB")
    private byte[] logo;
    
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

    public BrandEntity clone() {
        return BrandEntity.builder()
                .brandReference(this.brandReference)
                .name(this.name)
                .logo(this.logo)
                .version(this.version)
                .aliasVersion(this.aliasVersion)
                .creationDate(this.creationDate)
                .endDate(this.endDate)
                .isObsolete(this.isObsolete)
                .lastModifyDate(this.lastModifyDate)
                .lastModifyUser(this.lastModifyUser)
                .build();
    }

    public BrandDto toDto() {
        return BrandDto.builder()
                .id(this.id)
                .reference(this.brandReference)
                .name(this.name)
                .logoBase64(this.logo != null ? Base64.getEncoder().encodeToString(this.logo) : null)
                .version(this.version)
                .aliasVersion(this.aliasVersion)
                .creationDate(this.creationDate != null ? this.creationDate.toLocalDateTime() : null)
                .endDate(this.endDate != null ? this.endDate.toLocalDateTime() : null)
                .isObsolete(this.isObsolete)
                .lastModifyDate(this.lastModifyDate != null ? this.lastModifyDate.toLocalDateTime() : null)
                .lastModifyUser(this.lastModifyUser != null ? this.lastModifyUser.toSafeDto() : null)
                .build();
    }
}
