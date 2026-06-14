package com.home_banking.auth_service.service;

import com.home_banking.auth_service.entity.RefreshToken;
import com.home_banking.auth_service.entity.User;

public interface IRefreshTokenService {
    RefreshToken generateRefreshToken(User user);
    RefreshToken validateAndRotate(String token);
}
