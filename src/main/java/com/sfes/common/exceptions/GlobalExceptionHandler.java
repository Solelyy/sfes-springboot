package com.sfes.common.exceptions;

import com.sfes.common.classes.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static ApiError buildError(HttpStatus status, String message) {
        return  ApiError.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(MaximumLoginAttempts.class)
    public ResponseEntity<ApiError> handleMaxLoginAttempts(MaximumLoginAttempts ex) {
        return  ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(buildError(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage()));
    }
}
