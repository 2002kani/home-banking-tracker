package com.home_banking.open_banking_service.service;

import java.util.UUID;

public interface IOpenBankingService{
    public String authAndSave(String code, String state, String bankName, String bankCountry, UUID userId);
}
