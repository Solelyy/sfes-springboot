package com.sfes.auth.controller;

import com.sfes.auth.dto.AuthResponse;
import com.sfes.auth.dto.AuthResult;
import com.sfes.auth.dto.LoginRequest;
import com.sfes.auth.service.AuthService;
import com.sfes.auth.service.CookieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResult result = authService.login(
                loginRequest.email(),
                loginRequest.password()
        );

        ResponseCookie cookie =
                cookieService.createCookie(result.token());

        AuthResponse response = new AuthResponse(
                result.user().getEmail(),
                result.user().getRole(),
                "Login successful"
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {

        ResponseCookie cookie = cookieService.removeCookie();

        AuthResponse response = new AuthResponse(
                "Logout successful"
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}