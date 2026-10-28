package com.sfes.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest (
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String password
)
{}
