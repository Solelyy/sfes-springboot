package com.sfes.registrar.department.dto;

import com.sfes.registrar.department.DepartmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDepartment (
        @NotBlank
        @Size(max = 25)
        String departmentCode,

        DepartmentStatus departmentStatus
)
{}
