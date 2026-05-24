package com.portable.microservices.ms_inventory.shared.infrastructure.security.jwt;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

@Component
public class JwtUtil {

    private static final String SECRET = "tu_clave_secreta_super_segura_de_al_menos_32_caracteres_12345";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public Claims validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token vacío o nulo");
        }
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getErrorReason(JwtException e) {
        if (e instanceof ExpiredJwtException) return "Token expirado";
        if (e instanceof MalformedJwtException) return "Token malformado";
        if (e instanceof SecurityException) return "Firma del token inválida";
        if (e instanceof UnsupportedJwtException) return "Tipo de token no soportado";
        return "Error de autenticación: " + e.getMessage();
    }
}
