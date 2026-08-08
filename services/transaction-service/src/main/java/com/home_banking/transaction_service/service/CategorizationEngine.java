package com.home_banking.transaction_service.service;

import com.home_banking.transaction_service.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorizationEngine {
    private final List<ICategoryResolver> resolvers;

    CategorizationResult categorize(Transaction tx, Long userId) {
        return resolvers.stream()
                .map(r -> r.resolve(tx, userId).map(c -> new CategorizationResult(c, r.source())))
                .flatMap(Optional::stream)
                .findFirst()
                .orElse(CategorizationResult.none());
    }
}
