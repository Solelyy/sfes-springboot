package com.sfes.auth.service;

import com.sfes.auth.dto.ResetPasswordResponse;
import com.sfes.common.exceptions.AccessDeniedException;
import com.sfes.common.exceptions.FrequentResetPasswordException;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final ResetPasswordEmailService resetPasswordEmailService;
    private final ResetPasswordRepository resetPasswordRepository;

    @Value("${reset-password.expiration}")
    private long expiration;

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

    @Async
    public void sendResetPasswordEmail(String email, String firstName, String rawToken) {
        resetPasswordEmailService.sendResetPasswordEmail(
                email, firstName, rawToken
        );

        log.info("Password reset email sent");
    }
}
