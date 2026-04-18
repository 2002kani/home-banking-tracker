package com.home_banking.open_banking_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_sessions")
public class BankSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id",  nullable = false,  unique = true)
    private String sessionId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_country")
    private String bankCountry;

    private String status;

    @Column(name = "valid_until")
    private Instant validUntil;

    @Column(name = "created_at")
    private Instant createdAt;
}
