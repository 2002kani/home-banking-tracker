package com.home_banking.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingsDto {
    private BigDecimal savingsRate;
    private BigDecimal savingsAmount;
}
