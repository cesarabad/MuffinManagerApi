package com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.dto;

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
public class PackagePrintLightDto {
    private int id;
    private String reference;
    private String description;
}
