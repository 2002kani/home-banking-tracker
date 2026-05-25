package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.dto.StartAuthResponse;

public interface IOpenBankingService {
    StartAuthResponse startAuth(String bank, String country, Long userId);
    String authAndSave(String code, String state, String bankName, String bankCountry);
}
