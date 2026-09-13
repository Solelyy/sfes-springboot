package com.sfes.superadmin.account.dto;

public record ResendInvitationResponse (
        String email,
        String firstName,
        String rawToken
)
{}
