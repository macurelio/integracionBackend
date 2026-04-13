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
        // Injecting mock values for the test
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "testSecretKeyTestSecretKeyTestSecretKeyTestSecretKeyTestSecretKey");
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", 3600000);
    }

    @Test
    void generateToken_ReturnsNonNullToken() {
        // Act
        String token = tokenProvider.generateToken("test@dominio.cl");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Verify it looks like a JWT (3 parts separated by dots)
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void generateToken_DifferentEmails_ProduceDifferentTokens() {
        // Act
        String token1 = tokenProvider.generateToken("user1@dominio.cl");
        String token2 = tokenProvider.generateToken("user2@dominio.cl");

        // Assert
        assertNotEquals(token1, token2);
    }
}
