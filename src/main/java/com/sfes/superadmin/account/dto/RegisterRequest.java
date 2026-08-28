package com.sfes.superadmin.account.dto;

import com.sfes.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder

public record RegisterRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String employeeId,

        @NotBlank
        String firstName,

        String middleName,

        @NotBlank
        String lastName,

        @NotNull
        Role role
) {}