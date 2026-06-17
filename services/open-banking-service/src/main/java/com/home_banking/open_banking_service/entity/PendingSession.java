package com.home_banking.open_banking_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pending_sessions")
public class PendingSession {

    @Id
    @Column(name = "state", nullable = false, unique = true)
    private String state;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "bank_name")
    private String bankName;
}
