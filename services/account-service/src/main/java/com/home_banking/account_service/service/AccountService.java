package com.home_banking.account_service.service;

import com.home_banking.account_service.client.OpenBankingClient;
import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.StartAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final OpenBankingClient openBankingClient;

    public BanksListResponse getAvailableBanks(String country){
        return openBankingClient.getBanks(country);
    }

    public StartAuthResponse startAuth(String bank, String country){
        return openBankingClient.startAuth(bank, country);
    }
}
