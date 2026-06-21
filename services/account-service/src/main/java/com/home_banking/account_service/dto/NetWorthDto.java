package com.home_banking.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetWorthDto {
    private BigDecimal totalBalance;
    private BigDecimal changeAbsolut;
    private BigDecimal changePercent;
}
