package com.home_banking.open_banking_service.dto.sessionResponses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TransactionsResponse {
    private List<TransactionDto> transactions;
}
