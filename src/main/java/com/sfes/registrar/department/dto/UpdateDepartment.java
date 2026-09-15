package com.sfes.registrar.department.dto;

import com.sfes.registrar.department.DepartmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDepartment (
        DepartmentStatus departmentStatus
)
{}
