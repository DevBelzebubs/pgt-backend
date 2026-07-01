package com.portable.erp.api_gateway.infrastructure.security;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

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

    public void validateToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token vacío o nulo");
        }
        Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
    }

    public String getErrorReason(Exception e) {
        if (e instanceof ExpiredJwtException) {
            return "Token expirado";
        }
        if (e instanceof MalformedJwtException) {
            return "Token malformado";
        }
        if (e instanceof SecurityException) {
            return "Firma del token inválida";
        }
        if (e instanceof UnsupportedJwtException) {
            return "Tipo de token no soportado";
        }
        if (e instanceof IllegalArgumentException) {
            return "Token vacío o nulo";
        }
        return "Error de autenticación: " + e.getMessage();
    }
}
