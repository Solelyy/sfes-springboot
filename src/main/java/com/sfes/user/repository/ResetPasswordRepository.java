package com.sfes.user.repository;

import com.sfes.user.entity.ResetPassword;
import java.util.Optional;

import com.sfes.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    Optional<ResetPassword> findByTokenHashed(String tokenHashed);
    Optional<ResetPassword> findTopByUserOrderByCreatedAtDesc(User user);
}
