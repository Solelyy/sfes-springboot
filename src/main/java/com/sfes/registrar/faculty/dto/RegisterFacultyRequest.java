package com.sfes.registrar.faculty.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterFacultyRequest(
        @NotBlank(message = "Employee ID is required")
        @Size(min = 1, max = 50, message = "Employee ID must be between 1 and 50 characters")
        String employeeId,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
        String email,

        @NotBlank(message = "First Name is required")
        @Size(min = 1, max = 100, message = "First Name must be between 1 and 100 characters")
        String firstName,

        @Size(max = 100, message = "Middle Name must not exceed 100 characters")
        String middleName,

        @NotBlank(message = "Last Name is required")
        @Size(min = 1, max = 100, message = "Last Name must be between 1 and 100 characters")
        String lastName,

        @NotBlank(message = "Department Code is required")
        @Size(min = 1, max = 25, message = "Department Code must be between 1 and 25 characters")
        String departmentCode
) {}
