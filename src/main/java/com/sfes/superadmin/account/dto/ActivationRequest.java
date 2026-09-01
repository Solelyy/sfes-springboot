package com.sfes.superadmin.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivationRequest (
      @NotBlank
      @Size(min = 8, max = 72)
      String password,

      @NotBlank
      @Size(min = 8, max = 72)
      String confirmPassword
)
{}
