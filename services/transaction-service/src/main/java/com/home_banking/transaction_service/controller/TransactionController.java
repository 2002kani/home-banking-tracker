package com.home_banking.transaction_service.controller;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(){
        List<TransactionDto> transactions =  transactionService.getTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionBetweenDate(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo
            ){
        List<TransactionDto> transactions = transactionService.getTransactionsByDate(dateFrom, dateTo);
        return ResponseEntity.ok(transactions);
    }

    // transaktionen je category anzeigeen lassen

    // transaktionen je typ anzeigen lassen (eingang-ausgang)
}
