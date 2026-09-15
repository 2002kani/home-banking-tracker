package com.home_banking.transaction_service.controller;

import com.home_banking.transaction_service.service.IUserDataService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Interner Endpunkt - nur für Service-zu-Service-Aufrufe.
Liegt bewusst nicht unter /api/v1, damit das api-gateway ihn nicht nach außen routet.
*/
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
public class InternalUserDataController {
    private final IUserDataService userDataService;

    @DeleteMapping("/{userId}/data")
    public ResponseEntity<Void> deleteUserData(@PathVariable Long userId) {
        userDataService.deleteUserData(userId);
        return ResponseEntity.noContent().build();
    }
}
