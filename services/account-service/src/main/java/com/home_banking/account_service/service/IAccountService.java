package com.home_banking.account_service.service;

import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.StartAuthResponse;
import com.home_banking.account_service.event.AccountUpdateEvent;

public interface IAccountService {
    public BanksListResponse getAvailableBanks(String country);
    public StartAuthResponse startAuth(String bank, String country);
    public void updateAccount(AccountUpdateEvent event);
}
