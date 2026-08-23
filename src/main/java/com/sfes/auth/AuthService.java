package com.sfes.auth;

import com.sfes.common.exceptions.MaximumLoginAttempts;
import com.sfes.employee.entity.Employee;
import com.sfes.employee.repository.EmployeeRepository;
import com.sfes.user.entity.User;
import com.sfes.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User authenticateUser(String email, String password) {
        User user= userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password"));

        int failedLoginAttempts = user.getFailedLoginAttempts();
        LocalDateTime timeNow = LocalDateTime.now();

        //1. check first if account is locked
        if (user.getLockedUntil() != null &&
                timeNow.isBefore(user.getLockedUntil())
        ) {
            throw new MaximumLoginAttempts("Too many login attempts. Please try again later.");
        }

        //2. check password
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            int newFailedLoginAttempts = failedLoginAttempts + 1;
            user.setFailedLoginAttempts(newFailedLoginAttempts);

            if (newFailedLoginAttempts >= 5) {
                user.setLockedUntil(timeNow.plusMinutes(5));
            }

            userRepository.save(user);

            throw new BadCredentialsException("Incorrect email or password");
        }

        //3. if successful login > reset attempts
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);

        userRepository.save(user);

        return user;
    }
}

