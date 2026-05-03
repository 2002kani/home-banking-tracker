package com.home_banking.auth_service.service;

public interface IJwtService {
    String extractUsername(String token);
}
