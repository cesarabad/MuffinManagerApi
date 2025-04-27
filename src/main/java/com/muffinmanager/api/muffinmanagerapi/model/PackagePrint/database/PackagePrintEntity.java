package com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.dto.PackagePrintDto;
import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.dto.PackagePrintLightDto;
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
@Table(name = "packageprint")
public class PackagePrintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "packageprintreference", length = 12, nullable = false)
    private String packagePrintReference;
    @Column(name = "description", length = 255, nullable = false)
    private String description;
    @Column(name = "lastmodifydate", nullable = false)
    private Timestamp lastModifyDate;
    @JoinColumn(name = "lastmodifyuserid")
    @ManyToOne
    private UserEntity lastModifyUser;

    public PackagePrintDto toDto() {
        return PackagePrintDto.builder()
            .id(id)
            .reference(packagePrintReference)
            .description(description)
            .lastModifyDate(lastModifyDate != null ? lastModifyDate.toLocalDateTime() : null)
            .lastModifyUser(lastModifyUser != null ? lastModifyUser.toSafeDto() : null)
            .build();
    
    }

    public PackagePrintLightDto toLightDto() {
        return PackagePrintLightDto.builder()
            .id(id)
            .reference(packagePrintReference)
            .description(description)
            .build();
    }
}
