package com.home_banking.transaction_service.event;

import com.home_banking.transaction_service.dto.PartyDto;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {
    private String sessionId;
    private String accountId;
    private String externalId;
    private Long userId;
    private String currency;
    private String amount;
    private PartyDto creditor;
    private PartyDto debtor;
    private CreditDebitIndicator type;
    private LocalDate bookingDate;
    private TransactionStatus status;
    private String mcc;
    private List<String> remittanceInformation;
}

