package com.sfes.registrar.department.dto;

import com.sfes.registrar.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateDepartment (
        @NotNull
        Status status
)
{}
