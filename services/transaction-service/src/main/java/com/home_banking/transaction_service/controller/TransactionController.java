package com.home_banking.transaction_service.controller;
import com.home_banking.transaction_service.dto.ExpensesDto;
import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.service.ITransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {
    private final ITransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) CreditDebitIndicator type,
            @RequestParam(required = false) Long categoryId
            ){
        return ResponseEntity.ok(transactionService.getTransactions(
                userId,
                dateFrom,
                dateTo,
                type,
                categoryId
        ));
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PatchMapping("/transaction/{id}")
    public ResponseEntity<Void> categorizeTransaction(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @RequestParam Long categoryId
    ){
        transactionService.categorizeTransaction(userId, id, categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/transaction/expenses")
    public ResponseEntity<ExpensesDto> getExpensesThisMonth(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId
    ){
        return ResponseEntity.ok(transactionService.getExpensesThisMonth(userId));
    }
}
