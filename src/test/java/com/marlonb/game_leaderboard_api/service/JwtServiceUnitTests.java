package com.marlonb.game_leaderboard_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceUnitTests {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService(
                "3p7n+4cY0F3I6F19d8O5jB9GqM9AjZs8T0mWTg4fTlo=", // test secret
                60000 // 1 min
        );
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
