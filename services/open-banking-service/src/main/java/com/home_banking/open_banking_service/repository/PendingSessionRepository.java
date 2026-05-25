package com.home_banking.open_banking_service.repository;

import com.home_banking.open_banking_service.entity.PendingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingSessionRepository extends JpaRepository<PendingSession, String> {
}
