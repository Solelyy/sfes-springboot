package com.sfes.superadmin.account.service;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.common.utility.TokenService;
import com.sfes.employee.entity.Employee;
import com.sfes.employee.repository.EmployeeRepository;
import com.sfes.superadmin.account.AccountInvitation;
import com.sfes.superadmin.account.AccountInvitationRepository;
import com.sfes.superadmin.account.dto.ResendInvitationResponse;
import com.sfes.user.entity.User;
import com.sfes.user.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResendInvitationService {
    private final AccountInvitationRepository accountInvitationRepository;
    private final TokenService tokenService;
    private final EmployeeRepository employeeRepository;

    @Value("${invitation.expiration}")
    private long invitationExpiration;

    @Transactional
    public ResendInvitationResponse resendInvitation(String employeeId){
        String normalizedEmployeeId = NormalizationUtil.normalizeToEmployeeId(employeeId);

        Employee employee = employeeRepository.findByEmployeeId(normalizedEmployeeId)
                .orElseThrow(() -> new InvalidRequestException("Invalid request"));

        User user = employee.getUser();

        if (user.getStatus() != Status.PENDING) {
            throw new InvalidRequestException("Account is not pending activation");
        }

        Optional <AccountInvitation> latestInvitation =
                accountInvitationRepository.findTopByUserOrderByCreatedAtDesc(user);

        Instant now = Instant.now();

        if (latestInvitation.isPresent()) {
            if (now.isBefore(latestInvitation.get().getExpiresAt())) {
                throw new InvalidRequestException("Latest invitation is not yet expired");
            }
        }

        String rawToken = tokenService.generateToken();

        AccountInvitation newInvitation = AccountInvitation.builder()
                .user(user)
                .tokenHashed(tokenService.hashToken(rawToken))
                .expiresAt(now.plusMillis(invitationExpiration))
                .build();
        accountInvitationRepository.save(newInvitation);

        return new ResendInvitationResponse(
                user.getEmail(),
                user.getEmployee().getFirstName(),
                rawToken
        );
    }
}
