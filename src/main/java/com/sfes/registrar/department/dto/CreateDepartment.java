package com.sfes.registrar.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDepartment(
        @NotBlank
        @Size(max = 100)
        String departmentName,

        @NotBlank
        @Size(max = 25)
        String departmentCode
) {}
