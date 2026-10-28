package com.sfes.registrar.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudentRegistration(
        @NotBlank(message = "Student ID is required")
        @Size(min = 1, max = 25, message = "Student ID must be between 1 and 25 characters")
        String studentId,

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

        @NotBlank(message = "Course Code is required")
        @Size(min = 1, max = 25, message = "Course Code must be between 1 and 25 characters")
        String courseCode
) {}
