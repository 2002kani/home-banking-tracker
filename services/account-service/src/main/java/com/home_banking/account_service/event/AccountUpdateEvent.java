package com.home_banking.account_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateEvent {
    private String sessionId;
    private String accountUid;
    private UUID userId;
    private String iban;
    private String balance;
    private String currency;
    private String name;
}
