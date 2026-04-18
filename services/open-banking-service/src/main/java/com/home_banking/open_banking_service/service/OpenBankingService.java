package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.AuthorizeSessionResponse;
import com.home_banking.open_banking_service.dto.sessionResponses.BalancesResponse;
import com.home_banking.open_banking_service.entity.BankAccount;
import com.home_banking.open_banking_service.entity.BankSession;
import com.home_banking.open_banking_service.event.AccountUpdateEvent;
import com.home_banking.open_banking_service.repository.BankAccountRepository;
import com.home_banking.open_banking_service.repository.BankSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class OpenBankingService implements IOpenBankingService {
    private final EnablebankingClient enablebankingClient;
    private final BankSessionRepository bankSessionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final KafkaPublisherService kafkaPublisherService;

    public OpenBankingService(EnablebankingClient enablebankingClient,
                              BankSessionRepository bankSessionRepository,
                              BankAccountRepository bankAccountRepository,
                              KafkaPublisherService kafkaPublisherService) {
        this.enablebankingClient = enablebankingClient;
        this.bankSessionRepository = bankSessionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.kafkaPublisherService = kafkaPublisherService;
    }

    @Override
    public String authAndSave(String code, String state, String bankName, String bankCountry, UUID userId) {
        AuthorizeSessionResponse resp = enablebankingClient.authorizeSession(code);

        BankSession session = BankSession.builder()
                .sessionId(resp.getSessionId())
                .userId(userId)
                .status("ACTIVE")
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
                        .userId(userId)
                        .iban(resp.getAccounts() != null ? account.getAccountId().getIban() : null)
                        .currency(account.getCurrency())
                        .name(account.getName())
                        .identificationHash(account.getIdentificationHash())
                        .build();
                bankAccountRepository.save(bankAccount);

                AccountUpdateEvent event = AccountUpdateEvent.builder()
                        .sessionId(session.getSessionId())
                        .accountUid(account.getUid())
                        .userId(userId)
                        .iban(bankAccount.getIban())
                        .currency(account.getCurrency())
                        .balance(getEventBalance(account.getUid()))
                        .name(account.getName())
                        .build();

                kafkaPublisherService.publishBalanceEvent(event);
            });
        }

        return resp.getSessionId();
    }

    private String getEventBalance(String accountUid) {
        BalancesResponse resp = enablebankingClient.getBalances(accountUid);
        if(resp == null ||resp.getBalances() == null){
            return null;
        }
        return resp.getBalances().get(0).getBalanceAmount().getAmount();
    }
}
