package com.home_banking.api_gateway.service;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class GatewayJwtService {
    private final RSAPublicKey publicKey;

    public GatewayJwtService(
            @Value("${jwt.auth-public-path}") Resource publicKeyResource,
            @Value("${jwt.access-token-expiration}") Long accessTokenExpiration
    ){
        this.publicKey = loadPublicKey(publicKeyResource);
    }

    public void validate(String token){
        Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token);
    }

    public RSAPublicKey loadPublicKey(Resource resource){
        try{
            byte[] keyBytes = Base64.getMimeDecoder().decode(resource.getInputStream().readAllBytes());
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        }catch(Exception e){
            throw new IllegalStateException("Invalid public key", e);
        }
    }
}
