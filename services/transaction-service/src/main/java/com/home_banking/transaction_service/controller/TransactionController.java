package com.home_banking.transaction_service.controller;

import com.home_banking.transaction_service.dto.TransactionDto;
import com.home_banking.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    // einzelne transaktionen abrufen können

    // transaktionen pro datum anzeigen lassen

    // transaktionen je category anzeigeen lassen

    // transaktionen je typ anzeigen lassen (eingang-ausgang)
}
