package com.sfes.superadmin.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountInvitationRepository extends JpaRepository<AccountInvitation, Long> {
    Optional<AccountInvitation> findByTokenHashed(String tokenHashed);
}
