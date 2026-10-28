package com.sfes.superadmin.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivationRequest (
      @NotBlank(message = "Username is required")
      @Size(min = 3, max = 25, message = "Username must be between 3 and 25 characters")
      String username,

      @NotBlank(message = "Password is required")
      @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
      String password,

      @NotBlank(message = "Confirm Password is required")
      @Size(min = 8, max = 72, message = "Confirm Password must be between 8 and 72 characters")
      String confirmPassword
)
{}
