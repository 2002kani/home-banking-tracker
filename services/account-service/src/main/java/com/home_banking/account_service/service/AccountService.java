package com.home_banking.account_service.service;

import com.home_banking.account_service.client.OpenBankingClient;
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
        // Prüfen ob account existiert.

        // ja: komplett builden und befüllen.
        // nein: nur balance builden und befüllen, sowie updatedAt aktualisieren

        // in db speichern
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
}
