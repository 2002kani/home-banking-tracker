package com.home_banking.auth_service.service;

import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface IJwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
