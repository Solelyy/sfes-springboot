package com.sfes.auth.controller;

import com.sfes.auth.dto.AuthResponse;
import com.sfes.auth.dto.AuthResult;
import com.sfes.auth.dto.AuthUser;
import com.sfes.auth.dto.LoginRequest;
import com.sfes.auth.service.AuthService;
import com.sfes.auth.service.CookieService;
import com.sfes.common.classes.ApiResponse;
import com.sfes.security.user.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                result.email(),
                result.role(),
                "Login successful"
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request) {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        ResponseCookie cookie = cookieService.removeCookie();

        try {
            authService.logout(token);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse("Logout successful"));

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse("Logout successful, but server-side token revocation failed"));
        }
    }

    @GetMapping("/me")
    public ApiResponse<AuthUser> getAuthUser(@AuthenticationPrincipal CustomUserDetails customUser) {

        if (customUser == null) {
            return new ApiResponse<>(
                    "User is unauthenticated",
                    null
            );
        }

        AuthUser authUser = authService.getAuthUser(customUser.getUsername());

        return new ApiResponse<>(
                "Successfully retrieved authenticated user.",
                authUser
        );
    }
}