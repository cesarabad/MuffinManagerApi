package com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto;

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
public class MuffinShapeListDto extends MuffinShapeDto {
    private int id;
    private boolean isObsolete;
    private UserSafeDto lastModifyUserName;

    public MuffinShapeListDto(String shapeReference, String description, int version, int id, boolean isObsolete,
            UserSafeDto lastModifyUserName) {
        super(shapeReference, description, version);
        this.id = id;
        this.isObsolete = isObsolete;
        this.lastModifyUserName = lastModifyUserName;
    }

}
