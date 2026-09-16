package com.sfes.registrar.faculty.dto;

import com.sfes.registrar.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFacultyRequest(
        @NotBlank
        String employeeId,

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
        Status status

) {
}
