package com.sfes.auth.dto;

import com.sfes.user.enums.Role;

public record AuthResponse(
        String email,
        Role role,
        String message
) {
    public AuthResponse(String message) {
        this(null, null, message);
    }
}