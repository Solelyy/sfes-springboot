package com.sfes.registrar.department.dto;

import com.sfes.registrar.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateDepartment (
        @NotBlank(message = "Department Name is required")
        @Size(min = 1, max = 100, message = "Department Name must be between 1 and 100 characters")
        String departmentName,

        @NotBlank(message = "Department Code is required")
        @Size(min = 1, max = 25, message = "Department Code must be between 1 and 25 characters")
        String departmentCode,

        @NotNull(message = "Status is required")
        Status status
)
{}
