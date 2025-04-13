package com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.dto;

import java.time.LocalDateTime;

import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PackagePrintDto {
    private int id;
    private String reference;
    private String description;
    private LocalDateTime lastModifyDate;
    private UserSafeDto lastModifyUser;
}
