package com.sfes.registrar.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudentRegistration(
        @NotBlank
        String studentId,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotBlank
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String middleName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotBlank
        @Size(max = 25)
        String departmentCode
) {}
