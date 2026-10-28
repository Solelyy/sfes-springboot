package com.sfes.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String password,

        @NotBlank(message = "Confirm Password is required")
        @Size(min = 8, max = 72, message = "Confirm Password must be between 8 and 72 characters")
        String confirmPassword
) {
}
