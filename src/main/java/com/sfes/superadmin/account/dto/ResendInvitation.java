package com.sfes.superadmin.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResendInvitation (
        @NotBlank(message = "Employee ID is required")
        @Size(min = 1, max = 25, message = "Employee ID must be between 1 and 25 characters")
        String employeeId
)
{}
