package com.home_banking.transaction_service.dto;

import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private String accountId;
    private String amount;
    private String currency;
    private String creditorName;
    private String debtorName;
    private CreditDebitIndicator type;
    private LocalDate bookingDate;
    private TransactionStatus status;
}
