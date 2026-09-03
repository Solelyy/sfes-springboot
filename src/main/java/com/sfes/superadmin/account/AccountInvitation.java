package com.sfes.superadmin.account;

import com.sfes.common.classes.BaseEntity;
import com.sfes.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "account_invitation")
public class AccountInvitation extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "token_hashed", nullable = false, unique = true, length = 70)
    private String tokenHashed;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    @Version
    private Integer version;
}
