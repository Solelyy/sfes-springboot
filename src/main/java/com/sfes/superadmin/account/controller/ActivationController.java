package com.sfes.superadmin.account.controller;

import com.sfes.common.classes.ApiMessage;
import com.sfes.superadmin.account.AccountInvitation;
import com.sfes.superadmin.account.dto.ActivationRequest;
import com.sfes.superadmin.account.dto.ActivationResponse;
import com.sfes.superadmin.account.dto.VerifyInvitationResponse;
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

    @GetMapping("/{token}")
    public VerifyInvitationResponse verifyInvitation(@PathVariable String token) {
        AccountInvitation invitation = activationService.verifyInvitation(token);

        return new VerifyInvitationResponse(
                "Invitation is valid",
                invitation.getUser().getEmail()
        );
    }

    @PostMapping("/{token}/activation")
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
