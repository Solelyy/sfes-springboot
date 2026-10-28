package com.sfes.superadmin.account.dto;

import com.sfes.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        @Size(min = 1, max = 255, message = "Email must not exceed 255 characters")
        String email,

        @NotBlank(message = "Employee ID is required")
        @Size(min = 1, max = 25, message = "Employee ID must be between 1 and 25 characters")
        String employeeId,

        @NotBlank(message = "First Name is required")
        @Size(min = 1, max = 100, message = "First Name must be between 1 and 100 characters")
        String firstName,

        @Size(max = 100, message = "Middle Name must not exceed 100 characters")
        String middleName,

        @NotBlank(message = "Last Name is required")
        @Size(max = 100, message = "Last Name must not exceed 100 characters")
        String lastName,

        @NotNull(message = "Role is required")
        Role role
) {}