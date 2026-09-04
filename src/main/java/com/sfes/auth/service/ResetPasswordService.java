package com.sfes.auth.service;

import com.sfes.auth.dto.ResetPasswordResponse;
import com.sfes.common.exceptions.*;
import com.sfes.common.utility.PasswordService;
import com.sfes.common.utility.TokenService;
import com.sfes.user.entity.ResetPassword;
import com.sfes.user.entity.User;
import com.sfes.user.enums.Status;
import com.sfes.user.repository.ResetPasswordRepository;
import com.sfes.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordService passwordService;
    private final PasswordEncoder passwordEncoder;

    @Value("${reset-password.expiration}")
    private long expiration;

    @Value("${reset-password.cooldown}")
    private long cooldown;

    private static final long PASSWORD_TIMEOUT_MINS = 5;

    @Transactional
    public ResetPasswordResponse createResetPasswordRequest(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null ) {
            log.info("Password reset requested for an unregistered email");
            return null;
        }

        if (user.getStatus() != Status.ACTIVE) {
            throw new AccessDeniedException("Only active accounts can change their passwords");
        }

        Instant now = Instant.now();

        if (user.getPasswordChangedAt() != null) {
            Instant timeout = user.getPasswordChangedAt().plus(PASSWORD_TIMEOUT_MINS, ChronoUnit.MINUTES);

            if (now.isBefore(timeout)) {
                throw new FrequentResetPasswordException("Password recently updated, try again later");
            }
        }

        Optional <ResetPassword> latestRequest =
                resetPasswordRepository.findTopByUserOrderByCreatedAtDesc(user);

        if (latestRequest.isPresent()) {
            Instant cooldownUntil = latestRequest.get()
                    .getCreatedAt()
                    .plus(cooldown, ChronoUnit.MILLIS);

            if (now.isBefore(cooldownUntil)) {
                throw new FrequentResetPasswordException(
                        "A password reset request was recently made. Please try again later"
                );
            }
        }

        String rawToken = tokenService.generateToken();

        ResetPassword resetPassword = ResetPassword.builder()
                .user(user)
                .tokenHashed(tokenService.hashToken(rawToken))
                .expiresAt(now.plusMillis(expiration))
                .build();
        resetPasswordRepository.save(resetPassword);

        log.info("Password reset request created");

        return new ResetPasswordResponse(user.getEmail(), user.getEmployee().getFirstName(), rawToken);

    }

    public ResetPassword verifyResetPasswordToken(String token){
        if (token == null || token.isBlank()) {
            throw new InvalidRequestException("Invalid request");
        }

        String hashedToken = tokenService.hashToken(token);

        ResetPassword resetPassword = resetPasswordRepository.findByTokenHashed(hashedToken)
                .orElseThrow(() -> new InvalidRequestException("Invalid reset password request"));

        if (resetPassword.getUsedAt() != null) {
            throw new InvalidTokenException("Password has already been reset");
        }

        Instant now = Instant.now();

        if (!now.isBefore(resetPassword.getExpiresAt())) {
            throw new ExpiredTokenException("Reset password already expired");
        }

        return  resetPassword;
    }

    @Transactional
    public ResetPasswordResponse resetPassword(String token, String password, String confirmPassword) {
        ResetPassword resetPassword = verifyResetPasswordToken(token);
        User user = resetPassword.getUser();

        Instant now = Instant.now();

        passwordService.checkPasswordsMatch(password, confirmPassword);

        String hashedPassword = passwordEncoder.encode(password);

        user.setPasswordChangedAt(now);
        user.setUpdatedAt(now);
        user.setHashedPassword(hashedPassword);

        resetPassword.setUsedAt(now);

        return new ResetPasswordResponse(
                user.getEmail(), user.getEmployee().getFirstName()
        );
    }
}
