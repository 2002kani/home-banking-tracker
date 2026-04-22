package com.home_banking.account_service.service;

import com.home_banking.account_service.client.OpenBankingClient;
import com.home_banking.account_service.dto.AccountDto;
import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.StartAuthResponse;
import com.home_banking.account_service.entitiy.Account;
import com.home_banking.account_service.event.AccountUpdateEvent;
import com.home_banking.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final OpenBankingClient openBankingClient;
    private final AccountRepository accountRepository;

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
        accountRepository.findByUidAndUserId(event.getAccountUid(), event.getUserId())
                .ifPresentOrElse(
                  existingAccount -> {
                      existingAccount.setBalance(event.getBalance());
                      existingAccount.setUpdatedAt(Instant.now());
                      accountRepository.save(existingAccount);
                  },
                        () -> {
                            Account account = Account.builder()
                                    .uid(event.getAccountUid())
                                    .sessionId(event.getSessionId())
                                    .userId(event.getUserId())
                                    .iban(event.getIban())
                                    .name(event.getName())
                                    .currency(event.getCurrency())
                                    .balance(event.getBalance())
                                    .createdAt(Instant.now())
                                    .updatedAt(Instant.now())
                                    .build();
                            accountRepository.save(account);
                        }
                );
    }

    @Override
    public AccountDto getAccount(Long id) {
        return accountRepository.findById(id)
                .map(account -> AccountDto.builder()
                        .balance(account.getBalance())
                        .iban(account.getIban())
                        .name(account.getName())
                        .currency(account.getCurrency())
                        .build())
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
