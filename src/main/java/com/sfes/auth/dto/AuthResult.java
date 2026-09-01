package com.sfes.auth.dto;

import com.sfes.user.entity.User;

public record AuthResult(
        User user,
        String token
) {
}