package com.sfes.superadmin.account.dto;

import com.sfes.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder

public record RegisterRequest(
        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotBlank
        @Size(max = 25)
        String employeeId,

        @NotBlank
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String middleName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotNull
        Role role
) {}