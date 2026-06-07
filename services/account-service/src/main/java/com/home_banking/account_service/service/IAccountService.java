package com.home_banking.account_service.service;

import com.home_banking.account_service.dto.AccountDto;
import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.StartAuthResponse;
import com.home_banking.account_service.event.AccountUpdateEvent;

import java.util.List;

public interface IAccountService {
    public BanksListResponse getAvailableBanks(String country);
    public StartAuthResponse startAuth(String bank, String country, Long userId);
    public void updateAccount(AccountUpdateEvent event);
    public List<AccountDto> getAccounts(Long userId);
    public AccountDto getAccount(Long id, Long userId);
}
