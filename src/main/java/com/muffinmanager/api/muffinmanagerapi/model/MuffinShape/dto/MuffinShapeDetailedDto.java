package com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto;

import java.time.LocalDateTime;

import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MuffinShapeDetailedDto extends MuffinShapeDto {

    private int id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isObsolete;
    private UserSafeDto lastModifyUser;

    public MuffinShapeDetailedDto(String shapeReference, String description, int version, int id, LocalDateTime startDate,
            LocalDateTime endDate, boolean isObsolete, UserSafeDto lastModifyUser) {
        super( shapeReference, description, version);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isObsolete = isObsolete;
        this.lastModifyUser = lastModifyUser;
    }

}
