package com.example.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    // Base64 encoded secret key (must be 256-bit for HS256)
    private static final String SECRET_KEY_BASE64 = "KmRlZXBpbnNpZGVpa25vd2ltaW5sb3Zld2l0aGhpbTExMSo=";

    private final SecretKey secretKey;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public JwtUtils() {
        // Decode Base64 key to SecretKey
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_BASE64));
    }

    public String generateJwtToken(String email) {
        return Jwts.builder()
                .subject(email) // Updated method
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey) // ✅ No need to specify SignatureAlgorithm anymore!
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey) // Updated method
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
