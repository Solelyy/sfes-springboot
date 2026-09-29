package com.sfes.auth.dto;

public record ResetPasswordResponse (
        Long emailDeliveryId,
        String email,
        String firstName,
        String rawToken
) {
    public ResetPasswordResponse(Long emailDeliveryId, String email, String firstName) {
        this(emailDeliveryId, email, firstName, null);
    }
}
