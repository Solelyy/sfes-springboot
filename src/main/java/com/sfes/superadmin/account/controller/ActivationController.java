package com.sfes.superadmin.account.controller;

import com.sfes.superadmin.account.dto.ActivationRequest;
import com.sfes.superadmin.account.service.ActivationService;
import com.sfes.auth.dto.AuthResponse;
import com.sfes.auth.dto.AuthResult;
import com.sfes.auth.service.CookieService;
import com.sfes.common.classes.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class ActivationController {
    private final ActivationService activationService;
    private final CookieService cookieService;

    @GetMapping("/{token}")
    public ApiMessage verifyInvitation(@PathVariable String token) {
        activationService.verifyInvitation(token);

        return new ApiMessage("Invitation is valid");
    }

    @PostMapping("/{token}/activation")
    public ResponseEntity<AuthResponse> activateAccount(@PathVariable String token, @Valid @RequestBody ActivationRequest request) {
        AuthResult activationResult = activationService.activateAccount(
                token, request.password(), request.confirmPassword()
        );

        ResponseCookie cookie = cookieService.createCookie(activationResult.token());

        AuthResponse authResponse = new AuthResponse(
                activationResult.user().getEmail(),
                activationResult.user().getRole(),
                "Account activated successfully."
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }
}
