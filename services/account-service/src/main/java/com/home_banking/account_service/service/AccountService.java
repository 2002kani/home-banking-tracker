package com.home_banking.account_service.service;

import com.home_banking.account_service.client.OpenBankingClient;
import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.StartAuthResponse;
import com.home_banking.account_service.event.AccountUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final OpenBankingClient openBankingClient;

    @Override
    public BanksListResponse getAvailableBanks(String country){
        return openBankingClient.getBanks(country);
    }

    @Override
    public StartAuthResponse startAuth(String bank, String country){
        return openBankingClient.startAuth(bank, country);
    }

    @Override
    public void updateAccount(AccountUpdateEvent event) {
        // TODO: to implement..
    }
}
