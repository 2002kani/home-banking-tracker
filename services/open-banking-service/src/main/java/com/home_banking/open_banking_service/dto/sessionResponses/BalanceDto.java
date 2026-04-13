package com.home_banking.open_banking_service.dto.sessionResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.home_banking.open_banking_service.enums.BalanceStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BalanceDto {
    private String name;

    @JsonProperty("balance_amount")
    private BalanceAmount balanceAmount;

    @JsonProperty("balance_type")
    private BalanceStatus balanceType;

    @JsonProperty("reference_date")
    private LocalDate refDate;
}
