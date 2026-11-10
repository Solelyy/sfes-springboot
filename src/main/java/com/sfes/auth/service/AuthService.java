package com.sfes.auth.service;

import com.sfes.auth.dto.AuthResult;
import com.sfes.auth.dto.AuthUser;
import com.sfes.common.exceptions.AccessDeniedException;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.exceptions.MaximumLoginAttemptsException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.security.jwt.JwtService;
import com.sfes.user.entity.User;
import com.sfes.user.enums.Status;
import com.sfes.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisService redisService;

    public AuthResult login(String email, String password) {
        User user = authenticate(email, password);
        String token = jwtService.generateToken(user.getEmail(), user.getTokenVersion());

        return new AuthResult(user.getEmail(), user.getRole(), token);
    }

    private User authenticate(String email, String password) {
        String normalizedEmail = NormalizationUtil.normalizeToLowerCase(email);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new BadCredentialsException("Incorrect email or password")
                );

        Instant now = Instant.now();

        // 1. check if the account is currently locked
        if (user.getLockedUntil() != null) {
            if (now.isBefore(user.getLockedUntil())) {
                throw new MaximumLoginAttemptsException(
                        "Too many login attempts. Please try again later."
                );
            }

            // lock has expired
            user.setLockedUntil(null);
            user.setFailedLoginAttempts(0);
        }

        //check if account is not active
        if (!(user.getStatus() == Status.ACTIVE)) {
            throw new AccessDeniedException("Account is not active");
        }

        // 2. check password
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            int newFailedLoginAttempts = user.getFailedLoginAttempts() + 1;

            user.setFailedLoginAttempts(newFailedLoginAttempts);

            if (newFailedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setLockedUntil(
                        now.plus(LOCK_DURATION_MINUTES, ChronoUnit.MINUTES)
                );
            }

            userRepository.save(user);

            throw new BadCredentialsException("Incorrect email or password");
        }

        // 3. Successful login
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setLastLogin(now);
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);

        return user;
    }

    public AuthUser getAuthUser(String email) {
        String normalizedEmail = NormalizationUtil.normalizeToLowerCase(email);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new InvalidRequestException("Invalid request"));

        return new AuthUser(
                user.getEmployee().getEmployeeId(),
                user.getEmail(),
                user.getRole(),
                user.getEmployee().getFirstName(),
                user.getEmployee().getLastName()
        );
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }

        try {
            Claims claims = jwtService.extractAllClaims(token);

            String jti = claims.get("jti", String.class);

            if (jti == null) {
                return;
            }

            long expirationTime = claims.getExpiration().getTime();
            long remainingTimeMillis = expirationTime - Instant.now().toEpochMilli();

            if (remainingTimeMillis > 0) {
                redisService.set(
                        "revoked:" + jti,
                        "true",
                        Duration.ofMillis(remainingTimeMillis)
                );
            }

        } catch (JwtException e) {
            log.debug("Failed to revoke token: {}", e.getMessage());
        }
    }
}