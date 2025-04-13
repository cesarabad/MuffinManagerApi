package com.muffinmanager.api.muffinmanagerapi.model.Brand.dto;

import java.time.LocalDateTime;

import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class BrandDto {
 
    private int id;
    private String reference;
    private String name;
    private String logoBase64;
    private int version;
    private String aliasVersion;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private boolean isObsolete;
    private LocalDateTime lastModifyDate;
    private UserSafeDto lastModifyUser;
}
