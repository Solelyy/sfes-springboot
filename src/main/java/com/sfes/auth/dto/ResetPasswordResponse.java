package com.sfes.auth.dto;

import com.sfes.user.entity.User;

public record ResetPasswordResponse (
        String email,
        String firstName,
        String rawToken
)
{}
