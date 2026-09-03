package com.sfes.superadmin.account.dto;

import com.sfes.user.enums.Role;

public record ActivationResponse(
        String email,
        Role role,
        String firstName,
        String jwtToken
)
{}
