package com.sfes.auth.controller;

import com.sfes.auth.dto.ResetPasswordResponse;
import com.sfes.auth.dto.VerifyEmailRequest;
import com.sfes.auth.service.ResetPasswordEmailService;
import com.sfes.auth.service.ResetPasswordService;
import com.sfes.common.classes.ApiMessage;
import com.sfes.superadmin.account.dto.ActivationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password-resets")
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;
    private final ResetPasswordEmailService emailService;

    @PostMapping("/email")
    public ApiMessage sendResetPasswordEmail(@Valid @RequestBody VerifyEmailRequest request) {
        ResetPasswordResponse result = resetPasswordService
                .createResetPasswordRequest(request.email());

        if (result != null) {
            emailService.sendResetPasswordEmail(
                    result.email(), result.firstName(), result.rawToken()
            );
        }

        return new ApiMessage(
                "If the email is associated with an account, a password reset link has been sent."
        );
    }

    @GetMapping("/email/{token}")
    public ApiMessage verifyResetPasswordToken(@PathVariable String token){
        resetPasswordService.verifyResetPasswordToken(token);

        return new ApiMessage("Valid reset password token");
    }

    @PatchMapping("/email/{token}/reset")
    public ApiMessage resetPassword(@PathVariable String token, @RequestBody ActivationRequest request) {
        ResetPasswordResponse result = resetPasswordService.resetPassword(
                token,
                request.password(),
                request.confirmPassword()
        );

        emailService.sendSuccessfulResetPassword(
                result.email(), result.firstName()
        );

        return new ApiMessage("Password successfully reset");
    }
}
