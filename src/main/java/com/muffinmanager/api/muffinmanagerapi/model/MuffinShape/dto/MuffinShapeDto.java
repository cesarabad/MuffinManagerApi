package com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MuffinShapeDto {
    protected String shapeReference;
    protected String description;
    protected int version;

}
