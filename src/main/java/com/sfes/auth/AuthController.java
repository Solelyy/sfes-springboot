package com.sfes.auth;

import com.sfes.auth.dto.AuthResponse;
import com.sfes.auth.dto.LoginRequest;
import com.sfes.auth.service.AuthService;
import com.sfes.auth.service.CookieService;
import com.sfes.security.jwt.JwtService;
import com.sfes.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    @Value("${cookie.sameSite}")
    private String sameSite;

    //login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = authService.authenticateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        String email = user.getEmail();
        String token = jwtService.generateToken(email);

        ResponseCookie cookie = cookieService.createCookie(token);

        AuthResponse authResponse = AuthResponse.builder()
                .id(user.getId())
                .email(email)
                .role(user.getRole())
                .message("Login successful")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    //logout
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
       ResponseCookie cookie = cookieService.removeCookie();

        AuthResponse authResponse = AuthResponse.builder()
                .message("Logout successfully")
                .build();

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }
}

