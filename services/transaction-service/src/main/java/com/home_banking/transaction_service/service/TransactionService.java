package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.dto.ExpensesDto;
import com.home_banking.transaction_service.dto.SavingsDto;
import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.entity.Category;
import com.home_banking.transaction_service.entity.Transaction;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.event.TransactionEvent;
import com.home_banking.transaction_service.mapper.TransactionMapper;
import com.home_banking.transaction_service.repository.CategoryRepository;
import com.home_banking.transaction_service.repository.TransactionRepository;
import com.home_banking.transaction_service.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<TransactionDto> getTransactions(Long userId, LocalDate from, LocalDate to, CreditDebitIndicator type,  Long categoryId) {
        Specification<Transaction> specs = Specification
                .where(TransactionSpecification.byUserId(userId))
                .and(TransactionSpecification.byDateBetween(from, to))
                .and(TransactionSpecification.byType(type))
                .and(TransactionSpecification.byCategory(categoryId));

        return transactionRepository.findAll(specs)
                .stream()
                .map(TransactionMapper::mapToDto)
                .toList();
    }

    @Override
    public void persistTransactions(TransactionEvent event) {
        try {
            transactionRepository.save(TransactionMapper.mapToEntity(event));
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate transaction ignored: {}", event.getExternalId());
        }
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionMapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public void categorizeTransaction(Long userId, Long id, Long categoryId) {
        Transaction transaction = transactionRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Category category = categoryRepository.findByIdForUser(categoryId, userId)
                .orElseThrow(() ->  new RuntimeException("Category not found"));

        transaction.setCategory(category);
        transactionRepository.save(transaction);
    }

    @Override
    public ExpensesDto getExpensesThisMonth(Long userId, CreditDebitIndicator type) {
        LocalDate firstOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastMonthStart = firstOfMonth.minusMonths(1);
        LocalDate lastMonthEnd = firstOfMonth.minusDays(1);

        // No specification here: since you first go through all transactions, then load them
        // only to sum the amount after that. A repository with @Query is more efficient
        // since you can immediently sum the transaction amounts up.
        BigDecimal expensesCurrentMonth = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                        userId, type, lastMonthStart, lastMonthEnd
        );

        LocalDate previousMonthStart = firstOfMonth.minusMonths(2);
        LocalDate previousMonthEnd = firstOfMonth.minusMonths(1).minusDays(1);
        BigDecimal lastMonth = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, type, previousMonthStart, previousMonthEnd
        );

        BigDecimal changePercent = calculatePercentChange(expensesCurrentMonth, lastMonth);

        return ExpensesDto.builder()
                .expenses(expensesCurrentMonth)
                .changePercent(changePercent)
                .build();
    }

    @Override
    public SavingsDto getSavingsLastMonth(Long userId) {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate lastMonthStart = lastMonth.atDay(1);
        LocalDate lastMonthEnd = lastMonth.atEndOfMonth();

        BigDecimal incomeLastMonth =  transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, CreditDebitIndicator.CRDT, lastMonthStart, lastMonthEnd
        );
        BigDecimal expensesLastMonth =  transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, CreditDebitIndicator.DBIT, lastMonthStart, lastMonthEnd
        );

        BigDecimal savingsRateThisMonth = calculateSavingsRate(incomeLastMonth, expensesLastMonth);

        BigDecimal savingsAmount = incomeLastMonth.subtract(expensesLastMonth);

        return SavingsDto.builder()
                .savingsRate(savingsRateThisMonth)
                .savingsAmount(savingsAmount)
                .build();
    }

    private BigDecimal calculateSavingsRate(BigDecimal income, BigDecimal expenses){
        if(income.compareTo(BigDecimal.ZERO)==0){
            return BigDecimal.ZERO;
        }
        return income.subtract(expenses)
                .divide(income, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal calculatePercentChange(BigDecimal current, BigDecimal previous){
        if(previous.compareTo(BigDecimal.ZERO) == 0){
            return null;
        }
        return current.
                subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
