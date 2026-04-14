package com.home_banking.open_banking_service.dto.sessionResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.home_banking.open_banking_service.enums.CreditDebitIndicator;
import com.home_banking.open_banking_service.enums.TransactionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TransactionDto {
    @JsonProperty("transaction_amount")
    private TransactionAmount transactionAmount;

    private PartyDto creditor;

    private PartyDto debtor;

    @JsonProperty("credit_debit_indicator")
    private CreditDebitIndicator type;

    @JsonProperty("booking_date")
    private LocalDate bookingDate;

    private TransactionStatus status;
}
