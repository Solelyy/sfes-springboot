package com.sfes.user.entity;

import com.sfes.common.classes.BaseEntity;
import com.sfes.employee.entity.Employee;
import com.sfes.user.enums.Role;
import com.sfes.user.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table (name = "users")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Role role;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Status status= Status.ACTIVE;

    @Builder.Default
    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts = 0;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @OneToOne(mappedBy = "user")
    private Employee employee;
}
