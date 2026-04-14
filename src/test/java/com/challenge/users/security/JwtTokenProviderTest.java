package com.challenge.users.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        // Inyectando valores simulados para la prueba
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "testSecretKeyTestSecretKeyTestSecretKeyTestSecretKeyTestSecretKey");
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", 3600000);
    }

    @Test
    void generateToken_ReturnsNonNullToken() {
        // Ejecutar
        String token = tokenProvider.generateToken("test@dominio.cl");

        // Verificar
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Verificar que tenga formato JWT (3 partes separadas por puntos)
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void generateToken_DifferentEmails_ProduceDifferentTokens() {
        // Ejecutar
        String token1 = tokenProvider.generateToken("user1@dominio.cl");
        String token2 = tokenProvider.generateToken("user2@dominio.cl");

        // Verificar
        assertNotEquals(token1, token2);
    }
}
