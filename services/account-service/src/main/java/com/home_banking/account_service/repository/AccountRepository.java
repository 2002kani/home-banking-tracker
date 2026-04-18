package com.home_banking.account_service.repository;

import com.home_banking.account_service.entitiy.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUidAndUserId(String uid, UUID userId);
}
