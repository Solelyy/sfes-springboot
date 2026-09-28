package com.sfes.superadmin.account.dto;

public record RegisterResult (
        Long emailDeliveryId,
        String email,
        String firstName,
        String rawToken
) {}
