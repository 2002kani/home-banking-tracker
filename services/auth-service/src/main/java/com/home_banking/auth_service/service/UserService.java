package com.home_banking.auth_service.service;

import com.home_banking.auth_service.client.UserClient;
import com.home_banking.auth_service.entity.User;
import com.home_banking.auth_service.repository.RefreshTokenRepository;
import com.home_banking.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserClient userClient;

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User nicht gefunden"));

        userClient.deleteTransactionData(userId);

        refreshTokenRepository.deleteAllByUserId(userId);
        userRepository.delete(user);
    }
}
