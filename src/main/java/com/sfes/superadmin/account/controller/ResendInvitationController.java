package com.sfes.superadmin.account.controller;

import com.sfes.common.classes.ApiMessage;
import com.sfes.superadmin.account.dto.ResendInvitation;
import com.sfes.superadmin.account.dto.ResendInvitationResponse;
import com.sfes.superadmin.account.service.EmailActivationService;
import com.sfes.superadmin.account.service.ResendInvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class ResendInvitationController {
    private final EmailActivationService emailActivationService;
    private final ResendInvitationService resendInvitationService;

    @PostMapping("/invitations/resend")
    public ApiMessage resendInvitation(@Valid @RequestBody ResendInvitation request) {
        ResendInvitationResponse result =
                resendInvitationService.resendInvitation(request.employeeId());

        emailActivationService.sendActivationEmail(result.email(), result.firstName(), result.rawToken());

        return new ApiMessage("Invitation resent successfully");
    }
}
