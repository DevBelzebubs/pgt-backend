package com.portable.erp.api_gateway.infrastructure.security;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private static final String SECRET = "tu_clave_secreta_super_segura_de_al_menos_32_caracteres_12345";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public void validateToken(final String token) {
        Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
    }
}
