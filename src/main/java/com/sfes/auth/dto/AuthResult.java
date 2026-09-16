package com.sfes.auth.dto;

import com.sfes.user.entity.User;
import com.sfes.user.enums.Role;

public record AuthResult(
        String email,
        Role role,
        String token
) {}