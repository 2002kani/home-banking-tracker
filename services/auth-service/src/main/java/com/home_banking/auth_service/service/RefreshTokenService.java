package com.home_banking.auth_service.service;

import com.home_banking.auth_service.entity.RefreshToken;
import com.home_banking.auth_service.entity.User;
import com.home_banking.auth_service.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final long expirationTime;

    public RefreshTokenService(
            @Value("${jwt.refresh-token-expiration}") long expirationTime, RefreshTokenRepository refreshTokenRepository
    ){
        this.expirationTime = expirationTime;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(expirationTime))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Override
    public RefreshToken validateAndRotate(String token) {
        RefreshToken existingToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (existingToken.isRevoked()) {
            // Token already used -> maybe theft
            revokeAllUserTokens(existingToken.getUser());
            throw new RuntimeException("Refresh token is revoked");
        }

        if(existingToken.getExpiresAt().isBefore(Instant.now())){
            throw new RuntimeException("Refresh Token is expired");
        }

        existingToken.setRevoked(true);
        refreshTokenRepository.save(existingToken);

        return generateRefreshToken(existingToken.getUser());
    }

    private void revokeAllUserTokens(User user) {
        List<RefreshToken> allTokens = refreshTokenRepository.findAllByUser(user);
        allTokens.forEach(t -> t.setRevoked(true));
        refreshTokenRepository.saveAll(allTokens);
    }
}
