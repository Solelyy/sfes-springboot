package com.sfes.common.exceptions;

import com.sfes.common.classes.ApiError;
import com.sfes.common.classes.ApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler(InvalidInvitationException.class)
    public ResponseEntity<ApiError> handleInvalidInvitation(InvalidInvitationException ex) {
        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiError> handlePasswordMismatch(PasswordMismatchException ex) {
        return  ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiMessage> handleOptimisticLockingFailure(
            ObjectOptimisticLockingFailureException ex
    ){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiMessage("Invitation has already been used"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiMessage> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(new ApiMessage("Invalid request"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedError(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred"
                ));
    }
}
