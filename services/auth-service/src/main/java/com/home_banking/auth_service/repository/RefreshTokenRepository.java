package com.home_banking.auth_service.repository;

import com.home_banking.auth_service.entity.RefreshToken;
import com.home_banking.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);
    List<RefreshToken> findAllByUser(User user);
}
