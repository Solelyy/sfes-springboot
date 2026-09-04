package com.sfes.auth.dto;

public record ResetPasswordResponse (
        String email,
        String firstName,
        String rawToken
) {
    public ResetPasswordResponse(String email, String firstName) {
        this(email, firstName, null);
    }
}
