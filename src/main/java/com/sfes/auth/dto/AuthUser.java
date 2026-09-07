package com.sfes.auth.dto;

import com.sfes.user.enums.Role;

public record AuthUser(
        String employeeId,
        String email,
        Role role,
        String firstName,
        String lastName
) {}
