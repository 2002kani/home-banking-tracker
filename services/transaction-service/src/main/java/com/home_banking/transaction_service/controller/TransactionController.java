package com.home_banking.transaction_service.controller;
import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.enums.CreditDebitIndicator;
import com.home_banking.transaction_service.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {
    private final ITransactionService transactionService;

    /*
    /* Important: Exchange current implementation of userId with more secure way.
    /* (Take a look at open tickets)
    */

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) CreditDebitIndicator type,
            @RequestParam(required = false) Long categoryId
            ){
        return ResponseEntity.ok(transactionService.getTransactions(
                UUID.fromString(userId),
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
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long id,
            @RequestParam Long categoryId
    ){
        transactionService.categorizeTransaction(UUID.fromString(userId), id, categoryId);
        return ResponseEntity.noContent().build();
    }
}
