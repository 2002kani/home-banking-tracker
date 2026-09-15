package com.home_banking.account_service.repository;

import com.home_banking.account_service.dto.NetWorthDto;
import com.home_banking.account_service.entitiy.AccountSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountSnapshotRepository extends JpaRepository<AccountSnapshot, Long> {
    Optional<AccountSnapshot> findByUserIdAndSnapshotYearAndSnapshotMonth(Long userId, Integer snapshotYear, Integer snapshotMonth);

    @Modifying
    @Query("DELETE FROM AccountSnapshot s WHERE s.userId = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);
}
