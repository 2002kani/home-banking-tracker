package com.home_banking.open_banking_service.service;

import com.home_banking.open_banking_service.client.EnablebankingClient;
import com.home_banking.open_banking_service.dto.sessionResponses.BalancesResponse;
import com.home_banking.open_banking_service.entity.BankAccount;
import com.home_banking.open_banking_service.entity.BankSession;
import com.home_banking.open_banking_service.event.AccountUpdateEvent;
import com.home_banking.open_banking_service.event.TransactionRawEvent;
import com.home_banking.open_banking_service.repository.BankAccountRepository;
import com.home_banking.open_banking_service.repository.BankSessionRepository;
import com.home_banking.open_banking_service.utils.IdBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService implements  ISchedulerService {
    private final BankSessionRepository bankSessionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EnablebankingClient enablebankingClient;
    private final KafkaPublisherService kafkaPublisherService;
    private final IdBuilder idBuilder;

    @Override
    @Scheduled(fixedRate = 10000)
    //@Scheduled(cron = "0 0 0 * * *")
    public void syncTransactions() {
        log.info("SYNC STARTED");
        // Get sessions and the bank account for each session
        List<BankSession> activeSessions = bankSessionRepository.findByStatus("ACTIVE");

        activeSessions.forEach(session -> {
            List<BankAccount> accounts = bankAccountRepository.findBySessionId(session.getSessionId());

            accounts.forEach(account -> {
                try{
                    if(account.getLastSyncAt() == null){
                        mapAccountTransaction(session, account, LocalDate.now().minusDays(90), LocalDate.now());
                    }else{
                        mapAccountTransaction(session, account, LocalDate.now().minusDays(1),  LocalDate.now());
                    }
                    account.setLastSyncAt(Instant.now());
                    bankAccountRepository.save(account);
                    mapAccountBalance(session, account);
                }catch(Exception e){
                    log.error("Sync failed for Account {}: {}", account.getAccountUid(), e.getMessage());
                }
            });
        });
    }

    private void mapAccountTransaction(BankSession session, BankAccount account, LocalDate dateFrom,  LocalDate dateTo) {
        var response = enablebankingClient.getTransactionsByDate(
                account.getAccountUid(),
                dateFrom,
                dateTo
        );

        if(response == null || response.getTransactions() == null) return;

        response.getTransactions().forEach(tx -> {
            TransactionRawEvent event = TransactionRawEvent.builder()
                    .sessionId(session.getSessionId())
                    .userId(session.getUserId())
                    .accountId(account.getAccountUid())
                    .externalId(idBuilder.buildTransactionId(tx))
                    .currency(tx.getTransactionAmount().getCurrency())
                    .amount(tx.getTransactionAmount().getAmount())
                    .creditor(tx.getCreditor())
                    .debtor(tx.getDebtor())
                    .type(tx.getType())
                    .bookingDate(tx.getBookingDate() != null ? tx.getBookingDate().toString() : null)
                    .status(tx.getStatus())
                    .build();

            kafkaPublisherService.publishTransactionEvent(event);
            log.info("event published with session: {}", event.getSessionId());
        });
    }

    private void mapAccountBalance(BankSession session, BankAccount account){
        BalancesResponse resp = enablebankingClient.getBalances(account.getAccountUid());

        if(resp != null && !resp.getBalances().isEmpty()){
            AccountUpdateEvent event = AccountUpdateEvent.builder()
                    .sessionId(session.getSessionId())
                    .accountUid(account.getAccountUid())
                    .userId(session.getUserId())
                    .iban(account.getIban())
                    .currency(resp.getBalances().get(0).getBalanceAmount().getCurrency())
                    .balance(resp.getBalances().get(0).getBalanceAmount().getAmount())
                    .name(account.getName())
                    .build();

            kafkaPublisherService.publishBalanceEvent(event);
        };
    }
}
