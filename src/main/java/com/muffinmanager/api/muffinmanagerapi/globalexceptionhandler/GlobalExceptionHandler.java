package com.muffinmanager.api.muffinmanagerapi.globalexceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) { 
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An unexpected error occurred: " + ex.getMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) { 
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Invalid argument: " + ex.getMessage());
        }

        @ExceptionHandler(ExpiredJwtException.class)
        public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) { 
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Token expired: " + ex.getMessage());
        }

        @ExceptionHandler(SignatureException.class)
        public ResponseEntity<String> handleSignatureException(SignatureException ex) { 
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Invalid token signature: " + ex.getMessage());
        }
}
