package com.home_banking.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService implements IJwtService {

    /*
    This method encodes the jwt payload in order to extract the username (email) out.
    */
    @Override
    public String extractUsername(String token) {
        String[] chunks = token.split("//.");
    }
}
