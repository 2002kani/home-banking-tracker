package com.home_banking.transaction_service.event;

import com.home_banking.transaction_service.dto.PartyDto;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {
    private String sessionId;
    private String accountId;
    private String externalId;
    private UUID userId;
    private String currency;
    private String amount;
    private PartyDto creditor;
    private PartyDto debtor;
    private CreditDebitIndicator type;
    private String bookingDate;
    private TransactionStatus status;
}

