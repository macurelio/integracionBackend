package com.challenge.users.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Componente para la generación y gestión de tokens JWT.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.security.jwt-secret}")
    private String jwtSecret;

    @Value("${app.security.jwt-expiration-ms}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT para el correo electrónico del usuario dado.
     *
     * @param email Correo electrónico del usuario.
     * @return Un token JWT firmado.
     */
    public String generateToken(String email) {
        log.debug("Generando token para: {}", email);
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
