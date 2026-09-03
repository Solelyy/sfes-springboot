package com.sfes.auth.controller;

import com.sfes.auth.dto.ResetPasswordResponse;
import com.sfes.auth.dto.VerifyEmailRequest;
import com.sfes.auth.service.ResetPasswordService;
import com.sfes.common.classes.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password-resets")
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/email")
    public ApiMessage sendResetPasswordEmail(@Valid @RequestBody VerifyEmailRequest request) {
        ResetPasswordResponse result = resetPasswordService
                .createResetPasswordRequest(request.email());

        if (result != null) {
            resetPasswordService.sendResetPasswordEmail(
                    result.email(), result.firstName(), result.rawToken()
            );
        }

        return new ApiMessage(
                "If the email is associated with an account, a password reset link has been sent."
        );
    }
}
