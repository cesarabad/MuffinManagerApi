package com.muffinmanager.api.muffinmanagerapi.model.ErrorResponse;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private int statusCode;
    private String message;
}
