package com.sfes.superadmin.account.dto;

public record ActivationResponse(
        Long emailDeliveryId,
        String email,
        String firstName
)
{}
