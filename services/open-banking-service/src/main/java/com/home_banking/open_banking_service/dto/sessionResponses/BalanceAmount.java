package com.home_banking.open_banking_service.dto.sessionResponses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BalanceAmount {
    private String currency;
    private String amount;
}
