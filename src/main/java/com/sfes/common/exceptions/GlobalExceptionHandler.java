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

    @ExceptionHandler(MaximumLoginAttemptsException.class)
    public ResponseEntity<ApiError> handleMaxLoginAttempts(MaximumLoginAttemptsException ex) {
        return  ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(buildError(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage()));
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAccAlreadyExists(AccountAlreadyExistsException ex) {
        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(InvalidAccountRoleException.class)
    public ResponseEntity<ApiError> handleInvalidAccountRole(InvalidAccountRoleException ex) {
        return  ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(buildError(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage()));
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ApiError> handleEmailSending(EmailSendingException ex) {
        return  ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(buildError(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage()));
    }
}
