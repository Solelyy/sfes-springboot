package com.sfes.registrar.student.dto;

import com.sfes.registrar.student.StudentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateStudentRequest(
        @NotBlank
        String studentId,

        @NotBlank @Email
        String email,

        @NotBlank
        String firstName,

        String middleName,

        @NotBlank
        String lastName,

        @NotBlank
        String departmentCode,

        @NotNull
        StudentStatus status
) {
}
