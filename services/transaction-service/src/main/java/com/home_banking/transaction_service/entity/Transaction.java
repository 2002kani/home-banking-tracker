package com.home_banking.transaction_service.entity;

import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private String amount;

    @Column(name = "creditor_name")
    private String creditorName;

    @Column(name = "debtor_name")
    private String debtorName;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CreditDebitIndicator type;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at")
    private Instant createdAt;
}