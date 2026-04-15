package com.home_banking.open_banking_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.home_banking.open_banking_service.dto.sessionResponses.PartyDto;
import com.home_banking.open_banking_service.dto.sessionResponses.TransactionAmount;
import com.home_banking.open_banking_service.enums.CreditDebitIndicator;
import com.home_banking.open_banking_service.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class TransactionRawEvent {
    private String transactionId;
    private String sessionId;
    private String accountId;
    private String externalId;
    private String currency;
    private String amount;
    private PartyDto creditor;
    private PartyDto debtor;
    private CreditDebitIndicator type;
    private LocalDate bookingDate;
    private TransactionStatus status;
}
