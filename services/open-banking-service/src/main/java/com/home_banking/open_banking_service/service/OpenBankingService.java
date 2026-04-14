package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AuthorizeSessionResponse;
import com.home_banking.open_banking_service.entity.BankAccount;
import com.home_banking.open_banking_service.entity.BankSession;
import com.home_banking.open_banking_service.repository.BankAccountRepository;
import com.home_banking.open_banking_service.repository.BankSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OpenBankingService implements IOpenBankingService {
    private final EnablebankingClient enablebankingClient;
    private final BankSessionRepository bankSessionRepository;
    private final BankAccountRepository bankAccountRepository;

    public OpenBankingService(EnablebankingClient enablebankingClient,
                              BankSessionRepository bankSessionRepository,
                              BankAccountRepository bankAccountRepository) {
        this.enablebankingClient = enablebankingClient;
        this.bankSessionRepository = bankSessionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public String authAndSave(String code, String state, String bankName, String bankCountry) {
        AuthorizeSessionResponse resp = enablebankingClient.authorizeSession(code);

        BankSession session = BankSession.builder()
                .sessionId(resp.getSessionId())
                .status(state)
                .bankName(bankName)
                .bankCountry(bankCountry)
                .validUntil(Instant.now().plus(90, ChronoUnit.DAYS))
                .createdAt(Instant.now())
                .build();
        bankSessionRepository.save(session);

        // Save account details into db too
        if(resp.getAccounts() != null){
            resp.getAccounts().forEach(account -> {
                BankAccount bankAccount = BankAccount.builder()
                        .sessionId(resp.getSessionId())
                        .accountUid(account.getUid())
                        .iban(resp.getAccounts() != null ? account.getAccountId().getIban() : null)
                        .currency(account.getCurrency())
                        .name(account.getName())
                        .identificationHash(account.getIdentificationHash())
                        .build();
                bankAccountRepository.save(bankAccount);
            });
        }

        return resp.getSessionId();
    }
}
