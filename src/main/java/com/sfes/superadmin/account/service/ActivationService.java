package com.sfes.superadmin.account.service;

import com.sfes.common.exceptions.ExpiredTokenException;
import com.sfes.common.exceptions.InvalidTokenException;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.PasswordService;
import com.sfes.common.utility.TokenService;
import com.sfes.superadmin.account.AccountInvitation;
import com.sfes.superadmin.account.AccountInvitationRepository;
import com.sfes.superadmin.account.dto.ActivationResponse;
import com.sfes.user.entity.User;
import com.sfes.user.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ActivationService {
    private final TokenService tokenService;
    private final AccountInvitationRepository accountInvitationRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;

    public AccountInvitation verifyInvitation(String token) {
        String hashedToken = tokenService.hashToken(token);

        AccountInvitation invitation = accountInvitationRepository.findByTokenHashed(hashedToken)
                .orElseThrow(() -> new InvalidRequestException("Invalid Invitation"));

        Instant now = Instant.now();

        if (invitation.getUsedAt() != null) {
            throw new InvalidTokenException("Account already activated");
        }

        if (!now.isBefore(invitation.getExpiresAt())) {
            throw new ExpiredTokenException("Invitation expired");
        }

        return invitation;
    }

    @Transactional
    public ActivationResponse activateAccount(String token, String password, String confirmPassword) {
        AccountInvitation accountInvitation = verifyInvitation(token);
        User user = accountInvitation.getUser();

        passwordService.checkPasswordsMatch(password, confirmPassword);

        String hashedPassword = passwordEncoder.encode(password);
        user.setHashedPassword(hashedPassword);
        user.setStatus(Status.ACTIVE);

        accountInvitation.setUsedAt(Instant.now());

        return new ActivationResponse(
                user.getEmail(),
                user.getEmployee().getFirstName()
        );
    }
}
