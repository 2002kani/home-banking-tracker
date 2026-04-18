package com.home_banking.account_service.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid", nullable = false)
    private String uid;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "iban")
    private String iban;

    @Column(name = "name")
    private String name;

    @Column(name = "currency")
    private String currency;

    // wird durch accounts.updated Kafka Event aktualisiert
    @Column(name = "balance")
    private String balance;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
