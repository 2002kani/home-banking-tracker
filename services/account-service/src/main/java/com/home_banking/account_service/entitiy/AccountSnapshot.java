package com.home_banking.account_service.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "account_snapshot")
public class AccountSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "total_balance", nullable = false)
    private BigDecimal totalBalance;

    @Column(name = "snapshot_year", nullable = false)
    private Integer snapshotYear;

    @Column(name = "snapshot_month", nullable = false)
    private Integer snapshotMonth;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
