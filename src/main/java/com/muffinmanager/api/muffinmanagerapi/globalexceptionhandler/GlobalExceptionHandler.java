package com.muffinmanager.api.muffinmanagerapi.globalexceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.muffinmanager.api.muffinmanagerapi.model.ErrorResponse.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) { 
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An unexpected error occurred")
                    .build());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) { 
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid argument")
                    .build());
        }

        @ExceptionHandler(ExpiredJwtException.class)
        public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) { 
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Token expired")
                    .build());
        }

        @ExceptionHandler(SignatureException.class)
        public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) { 
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid token")
                    .build());
        }
}
