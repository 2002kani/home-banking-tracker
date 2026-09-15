package com.home_banking.open_banking_service.repository;

import com.home_banking.open_banking_service.entity.PendingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingSessionRepository extends JpaRepository<PendingSession, String> {
    @Modifying
    @Query("DELETE FROM PendingSession s WHERE s.userId = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);
}
