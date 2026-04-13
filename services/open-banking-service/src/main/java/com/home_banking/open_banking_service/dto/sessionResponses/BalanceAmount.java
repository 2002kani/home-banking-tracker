package com.home_banking.open_banking_service.dto.sessionResponses;

import lombok.Data;

@Data
public class BalanceAmount {
    private String currency;
    private String amount;
}
