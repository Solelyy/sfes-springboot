package com.sfes.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @Size(min = 8, max = 72)
        String currentPassword,

        @NotBlank
        String token
)
{}
