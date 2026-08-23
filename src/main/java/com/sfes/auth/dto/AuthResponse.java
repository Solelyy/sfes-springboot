package com.sfes.auth.dto;

import com.sfes.user.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private Long id;
    private Role role;
    private String email;
    private String message;
}

