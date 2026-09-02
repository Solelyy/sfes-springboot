package com.sfes.superadmin.account.service;

import com.sfes.auth.dto.AuthResult;
import com.sfes.common.exceptions.InvalidInvitationException;
import com.sfes.common.exceptions.PasswordMismatchException;
import com.sfes.security.jwt.JwtService;
import com.sfes.superadmin.account.AccountInvitation;
import com.sfes.superadmin.account.AccountInvitationRepository;
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
    private final InvitationTokenService invitationTokenService;
    private final AccountInvitationRepository accountInvitationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SuccessfulActivationService successfulActivationService;

    public AccountInvitation verifyInvitation(String token) {
        String hashedToken = invitationTokenService.hashToken(token);

        AccountInvitation invitation = accountInvitationRepository.findByTokenHashed(hashedToken)
                .orElseThrow(() -> new InvalidInvitationException("Invalid Invitation"));

        Instant now = Instant.now();

        if (invitation.getUsedAt() != null) {
            throw new InvalidInvitationException("Account already activated");
        }

        if (!now.isBefore(invitation.getExpiresAt())) {
            throw new InvalidInvitationException("Invitation expired");
        }

        return invitation;
    }

    private void checkValidPassword(String password, String confirmPassword){
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }

    @Transactional
    public AuthResult activateAccount(String token, String password, String confirmPassword) {
        AccountInvitation accountInvitation = verifyInvitation(token);
        User user = accountInvitation.getUser();

        checkValidPassword(password, confirmPassword);

        String hashedPassword = passwordEncoder.encode(password);
        user.setHashedPassword(hashedPassword);
        user.setStatus(Status.ACTIVE);

        accountInvitation.setUsedAt(Instant.now());

        successfulActivationService.sendSuccessActivation(
                user.getEmail(), user.getEmployee().getFirstName()
        );

        String jwtToken = jwtService.generateToken(user.getEmail());
        return new AuthResult(user, jwtToken);
    }
}
