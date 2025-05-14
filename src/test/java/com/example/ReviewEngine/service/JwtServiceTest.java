package com.example.ReviewEngine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // inject secret and expiration
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1_000L);
    }

    @Test
    void generateAndValidateToken_success() {
        String token = jwtService.generateToken("bob");
        assertThat(token).isNotBlank();

        assertThat(jwtService.validateToken(token)).isTrue();
        assertThat(jwtService.getUsernameFromToken(token)).isEqualTo("bob");
    }

    @Test
    void validateToken_expired() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 10L);
        String token = jwtService.generateToken("bob");
        Thread.sleep(15);
        assertThat(jwtService.validateToken(token)).isFalse();
    }
}
