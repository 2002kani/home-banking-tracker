package com.home_banking.transaction_service.controller;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class TransactionController {
    private final TransactionService transactionService;

    /*
    /* Important: Exchange current implementation of userId with more secure way.
    /* (Take a look at open tickets)
    */

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ){
        if(dateFrom != null && dateTo != null){
            List<TransactionDto> transactions = transactionService.getTransactionsByDate(dateFrom, dateTo, UUID.fromString(userId));
            return ResponseEntity.ok(transactions);
        }
        return ResponseEntity.ok(transactionService.getTransactions(UUID.fromString(userId)));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    // transaktionen je category anzeigeen lassen

    // transaktionen je typ anzeigen lassen (eingang-ausgang)
}
