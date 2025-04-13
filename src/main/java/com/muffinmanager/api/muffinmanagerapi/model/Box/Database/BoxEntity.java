package com.muffinmanager.api.muffinmanagerapi.model.Box.Database;

import java.sql.Timestamp;

import com.muffinmanager.api.muffinmanagerapi.model.Box.dto.BoxDto;
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
@Table(name = "box")
public class BoxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "boxreference", length = 20, nullable = false)
    private String boxReference;
    @Column(name = "description", length = 80, nullable = false)
    private String description;
    @Column(name = "europeanbase")
    private Integer europeanBase;
    @Column(name = "americanbase")
    private Integer americanBase;
    @Column(name = "defaultheight")
    private Integer defaultHeight;
    @Column(name = "lastmodifydate", nullable = false)
    private Timestamp lastModifyDate;
    @ManyToOne
    @JoinColumn(name = "lastmodifyuserid")
    private UserEntity lastModifyUser;

    public BoxEntity clone() {
        return BoxEntity.builder()
                .boxReference(this.boxReference)
                .description(this.description)
                .europeanBase(this.europeanBase != null ? this.europeanBase : null)
                .americanBase(this.americanBase != null ? this.americanBase : null)
                .defaultHeight(this.defaultHeight != null ? this.defaultHeight : null)
                .lastModifyDate(this.lastModifyDate != null ? this.lastModifyDate : null)
                .lastModifyUser(this.lastModifyUser != null ? this.lastModifyUser : null)
                .build();
    }

    public BoxDto toDto() {
        return BoxDto.builder()
                .id(this.id)
                .reference(this.boxReference)
                .description(this.description)
                .europeanBase(this.europeanBase != null ? this.europeanBase : null)
                .americanBase(this.americanBase != null ? this.americanBase : null)
                .defaultHeight(this.defaultHeight != null ? this.defaultHeight : null)
                .lastModifyDate(this.lastModifyDate != null ? this.lastModifyDate.toLocalDateTime() : null)
                .lastModifyUser(this.lastModifyUser != null ? this.lastModifyUser.toSafeDto() : null)
                .build();
    }
}