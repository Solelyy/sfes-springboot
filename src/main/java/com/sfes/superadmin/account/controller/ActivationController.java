package com.sfes.superadmin.account.controller;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.superadmin.account.dto.ActivationRequest;
import com.sfes.superadmin.account.dto.ActivationResponse;
import com.sfes.superadmin.account.service.ActivationService;
import com.sfes.superadmin.account.service.SuccessfulActivationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class ActivationController {
    private final ActivationService activationService;
    private final SuccessfulActivationService successfulActivationService;

    @GetMapping("/verify/{token}")
    public ApiResponse<String> verifyInvitation(@PathVariable String token) {
        String email = activationService.verifyInvitation(token);

        return new ApiResponse<>(
                "Invitation is valid",
                email
        );
    }

    @PostMapping("/activate/{token}")
    public ApiMessage activateAccount(@PathVariable String token, @Valid @RequestBody ActivationRequest request) {
        ActivationResponse result = activationService.activateAccount(
                token, request.password(), request.confirmPassword()
        );

        successfulActivationService.sendSuccessActivation(result.email(), result.firstName());

        return new ApiMessage(
                "Account activated successfully."
        );
    }
}
