package com.sfes.superadmin.management;

import com.sfes.common.exceptions.AccountNotFoundException;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.employee.entity.Employee;
import com.sfes.employee.repository.EmployeeRepository;
import com.sfes.superadmin.account.AccountInvitationRepository;
import com.sfes.user.entity.User;
import com.sfes.user.enums.Role;
import com.sfes.user.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final EmployeeRepository employeeRepository;
    private final AccountInvitationRepository accountInvitationRepository;

    public AccountsResponse getAccounts(Role role) {
        List<AccountsResponse.Account> accounts =
                employeeRepository.findAccounts(role)
                        .stream()
                        .map(account -> new AccountsResponse.Account(
                                account.getEmployeeId(),
                                account.getEmail(),
                                account.getFirstName(),
                                account.getMiddleName(),
                                account.getLastName(),
                                account.getRole(),
                                account.getStatus(),
                                account.getStatus() == Status.PENDING
                                        ? account.getExpiresAt()
                                        : null
                        ))
                        .toList();
        return new AccountsResponse(accounts);
    }

    @Transactional
    public void updateAccountStatus(String employeeId, Status newStatus) {
        String normalizedEmployeeId = NormalizationUtil.normalizeToEmployeeId(employeeId);

        Employee employee = employeeRepository.findByEmployeeId(normalizedEmployeeId)
                .orElseThrow(() -> new AccountNotFoundException("Employee does not exist"));

        User user = employee.getUser();

        Status currentStatus = user.getStatus();

        if (currentStatus == newStatus) {
            throw new InvalidRequestException(
                    "Change status request cannot be the same as the current status."
            );
        }

        if(!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidRequestException(
                    "Cannot change account status from %s to %s"
                            .formatted(currentStatus, newStatus)
            );
        }

        if (currentStatus == Status.PENDING && newStatus == Status.REMOVED) {
            accountInvitationRepository
                    .findTopByUserOrderByCreatedAtDesc(user)
                    .ifPresent(invitation -> {
                        invitation.setRevokedAt(Instant.now());
                    });
        }

        user.setStatus(newStatus);
        user.setTokenVersion(user.getTokenVersion() + 1);
    }
}
