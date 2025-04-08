package com.muffinmanager.api.muffinmanagerapi.model.Box;

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
public class BoxDto {
    
    private int id;
    private String reference;
    private String description;
    private Integer europeanBase;
    private Integer americanBase;
    private Integer defaultHeight;
    private LocalDateTime lastModifyDate;
    private UserSafeDto lastModifyUser;
}
