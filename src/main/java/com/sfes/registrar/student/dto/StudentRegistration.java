package com.sfes.registrar.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentRegistration(
        @NotBlank
        String studentId,

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
