package com.home_banking.open_banking_service.repository;

import com.home_banking.open_banking_service.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findBySessionId(String sessionId);
}
