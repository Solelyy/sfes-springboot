package com.sfes.superadmin.management;

import com.sfes.user.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateStatusRequest(
        @NotNull
        Status status,

        @NotBlank
        @Size(max = 25)
        String employeeId
) {}
