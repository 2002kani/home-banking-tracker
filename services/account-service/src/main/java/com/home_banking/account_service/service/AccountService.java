package com.home_banking.account_service.service;

import com.home_banking.account_service.client.OpenBankingClient;
import com.home_banking.account_service.dto.AccountDto;
import com.home_banking.account_service.dto.BanksListResponse;
import com.home_banking.account_service.dto.NetWorthDto;
import com.home_banking.account_service.dto.StartAuthResponse;
import com.home_banking.account_service.entitiy.Account;
import com.home_banking.account_service.entitiy.AccountSnapshot;
import com.home_banking.account_service.event.AccountUpdateEvent;
import com.home_banking.account_service.repository.AccountRepository;
import com.home_banking.account_service.repository.AccountSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final OpenBankingClient openBankingClient;
    private final AccountRepository accountRepository;
    private final AccountSnapshotRepository accountSnapshotRepository;

    @Override
    public BanksListResponse getAvailableBanks(String country){
        return openBankingClient.getBanks(country);
    }

    @Override
    public StartAuthResponse startAuth(String bank, String country, Long userId){
        return openBankingClient.startAuth(bank, country, userId);
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
    public List<AccountDto> getAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId)
                .stream()
                .map(acc -> AccountDto.builder()
                        .iban(acc.getIban())
                        .balance(acc.getBalance())
                        .name(acc.getName())
                        .currency(acc.getCurrency())
                        .build())
                .toList();
    }

    @Override
    public AccountDto getAccount(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId)
                .map(account -> AccountDto.builder()
                        .balance(account.getBalance())
                        .iban(account.getIban())
                        .name(account.getName())
                        .currency(account.getCurrency())
                        .build())
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public void upsertNetWorthSnapshot(Long userId) {
        // adds all account worths from user up, every day (kafka consume)
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        BigDecimal totalNetWorth = accounts
                .stream()
                .map(acc -> new BigDecimal(acc.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime now = LocalDateTime.now();
        accountSnapshotRepository.findByUserIdAndSnapshotYearAndSnapshotMonth(userId, now.getYear(), now.getMonthValue())
                .ifPresentOrElse(
                        existing -> {
                            existing.setTotalBalance(totalNetWorth);
                            existing.setUpdatedAt(Instant.now());
                            accountSnapshotRepository.save(existing);
                        }, () ->
                            accountSnapshotRepository.save(AccountSnapshot.builder()
                                    .userId(userId)
                                    .totalBalance(totalNetWorth)
                                    .snapshotYear(now.getYear())
                                    .snapshotMonth(now.getMonthValue())
                                    .updatedAt(Instant.now())
                                    .build())
                );
    }

    @Override
    public NetWorthDto getNetWorth(Long userId) {
        BigDecimal currentTotal = accountRepository.findAllByUserId(userId)
                .stream()
                .map(acc -> new BigDecimal(acc.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        Optional<AccountSnapshot> lastMonthSnapshot = accountSnapshotRepository
                .findByUserIdAndSnapshotYearAndSnapshotMonth(
                        userId,
                        lastMonth.getYear(),
                        lastMonth.getMonthValue()
                );

        // Kein Vormonat vorhanden → neuer User oder erste Verbindung
        // Frontend soll in diesem Fall keinen Vergleich anzeigen
        if(lastMonthSnapshot.isEmpty()) {
            return NetWorthDto.builder()
                    .totalBalance(currentTotal)
                    .changeAbsolut(null)
                    .changePercent(null)
                    .build();
        }

        BigDecimal changeAbsolut = currentTotal.subtract(lastMonthSnapshot.get().getTotalBalance());
        BigDecimal changePercent = changeAbsolut
                .divide(lastMonthSnapshot.get().getTotalBalance(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return NetWorthDto.builder()
                .totalBalance(currentTotal)
                .changePercent(changePercent)
                .changeAbsolut(changeAbsolut)
                .build();
    }
}
