package com.muffinmanager.api.muffinmanagerapi.model.WSMessage;

import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WebSocketMessage {
    private String dictionaryKey;
    private UserSafeDto user;
}
