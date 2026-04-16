package com.home_banking.open_banking_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateEvent {
    private String sessionId;
    private String accountUid;
    private String iban;
    private String balance;
    private String currency;
}
