package com.sfes.user.entity;

import com.sfes.common.classes.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name = "reset_password")
@Entity
public class ResetPassword extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
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
