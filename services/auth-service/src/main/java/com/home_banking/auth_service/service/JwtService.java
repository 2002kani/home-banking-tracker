package com.home_banking.auth_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService implements IJwtService {
    @Value("${jwt.auth-private-path}")
    private Resource privateKeyResource;

    /*
    This method encodes the jwt payload in order to extract the username (email) out
    The username lives in the "subject" field of the payload.
    */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extractor, which lets you easily extract claims from the token
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllCLaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllCLaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        try{
            byte[] keyBytes = Base64.getDecoder().decode(extractJwtSecret(privateKeyResource));
            return Keys.hmacShaKeyFor(keyBytes);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private String extractJwtSecret(Resource resource) throws IOException {
        return new String(resource.getInputStream().readAllBytes());
    }
}
