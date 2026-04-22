package com.home_banking.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class AccountDto {
    private String iban;
    private String name;
    private String currency;
    private String balance;
}
