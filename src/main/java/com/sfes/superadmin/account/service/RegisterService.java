package com.sfes.superadmin.account.service;

import com.sfes.common.exceptions.AccountAlreadyExistsException;
import com.sfes.common.exceptions.InvalidAccountRoleException;
import com.sfes.common.utility.TokenService;
import com.sfes.employee.entity.Employee;
import com.sfes.employee.repository.EmployeeRepository;
import com.sfes.superadmin.account.AccountInvitationRepository;
import com.sfes.superadmin.account.dto.RegisterRequest;
import com.sfes.superadmin.account.AccountInvitation;
import com.sfes.user.entity.User;
import com.sfes.user.enums.Role;
import com.sfes.user.enums.Status;
import com.sfes.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AccountInvitationRepository accountInvitationRepository;

    @Value("${invitation.expiration}")
    private long invitationExpiration;

    @Transactional
    public String registerUser(RegisterRequest request) {
        if (request.role() != Role.GUIDANCE && request.role() !=Role.REGISTRAR) {
            throw new InvalidAccountRoleException("Only Registrar and Guidance accounts can be created.");
        }

        if (employeeRepository.findByEmployeeId(request.employeeId()).isPresent()) {
            throw new AccountAlreadyExistsException("Employee ID already exists.");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AccountAlreadyExistsException("Email already exists.");
        }

        User user = User.builder()
                .email(request.email())
                .role(request.role())
                .status(Status.PENDING)
                .build();
        userRepository.save(user);

        Employee employee = Employee.builder()
                .employeeId(request.employeeId())
                .firstName(request.firstName())
                .middleName(request.middleName())
                .lastName(request.lastName())
                .user(user)
                .build();
        employeeRepository.save(employee);

        Instant now = Instant.now();
        String rawToken = tokenService.generateToken();

        AccountInvitation accountInvitation = AccountInvitation.builder()
                .user(user)
                .tokenHashed(tokenService.hashToken(rawToken))
                .expiresAt(now.plusMillis(invitationExpiration))
                .build();
        accountInvitationRepository.save(accountInvitation);

        return rawToken;
    }
}
