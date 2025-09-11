package com.marlonb.game_leaderboard_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceUnitTests {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should generate token for valid username")
        void shouldGenerateTokenForValidUsername () {

            String testUsername = "username";

            String token = jwtService.generateToken(testUsername);

            assertNotNull(token);

            assertFalse(token.isEmpty());

        }
    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("Should handle empty username")
        void shouldHandleEmptyUsername () {
            assertDoesNotThrow(() -> jwtService.generateToken(""));
        }
    }

}
