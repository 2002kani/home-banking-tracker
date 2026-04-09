package com.home_banking.open_banking_service.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class EnableBankingJwtProvider {
    @Value("${enablebanking.api.application-id}")
    private String applicationId;

    @Value("${enablebanking.api.private-key-path}")
    private Resource privateKeyResource;

    private PrivateKey privateKey;

    /*
     * @PostConstruct performs init() method once at the beginning of starting the application
     * after inititalising the bean.
     * The method reads the private key through the pem file and removes header, footer
     * and whitespaces and also converts the base64 encoded content to valid RSA key
     */
    @PostConstruct
    public void init() throws Exception {
        String pem = new String(privateKeyResource.getInputStream().readAllBytes());

        String base64 = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(base64);
        this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

        log.info("Enable Banking Private Key geladen");
    }

    public String generateJwt(){
        long timeNow = System.currentTimeMillis() / 1000;

        return Jwts.builder()
                .header()
                    .add("kid", applicationId)
                    .add("typ", "JWT")
                    .and()
                .issuer("enablebanking.com")
                .claim("aud", "api.enablebanking.com")  // ← String, kein Array
                .claim("iat", timeNow)
                .claim("exp", timeNow + 3600)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
