package com.sfes.superadmin.management;

import com.sfes.user.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateStatusRequest(
        @NotNull(message = "Status is required")
        Status status,

        @NotBlank(message = "Employee ID is required")
        @Size(max = 25, message = "Employee ID must not exceed 25 characters")
        String employeeId
) {}
