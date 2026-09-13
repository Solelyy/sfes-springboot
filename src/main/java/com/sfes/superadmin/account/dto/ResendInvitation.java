package com.sfes.superadmin.account.dto;

import jakarta.validation.constraints.NotBlank;

public record ResendInvitation (
        @NotBlank
        String employeeId
)
{}
