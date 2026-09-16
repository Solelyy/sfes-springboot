package com.sfes.registrar.faculty.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterFacultyRequest(
        @NotBlank
        String employeeId,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String firstName,

        String middleName,

        @NotBlank
        String lastName,

        @NotBlank
        String departmentCode
) {}
