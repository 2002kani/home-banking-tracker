package com.home_banking.auth_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements IJwtService {
    private final Long accessTokenExpiration;
    private final SecretKey signingKey;

    public JwtService(
            @Value("${jwt.access-token-expiration}") Long accessTokenExpiration,
            @Value("${jwt.auth-private-path}") Resource privateKeyResource
    ) {
        this.accessTokenExpiration = accessTokenExpiration;
        this.signingKey = loadKey(privateKeyResource);
    }

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

    public String generateToken(UserDetails userDetails) {
        return generateJwt(new HashMap<>(), userDetails);
    }

    // Header gets built automatically
    @Override
    public String generateJwt(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllCLaims(String token){
        return Jwts
                .parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey loadKey(Resource resource) {
        try{
            String secret = new String(resource.getInputStream().readAllBytes());
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch(Exception e){
            throw new IllegalStateException("Failed to load JWT key", e);
        }
    }
}