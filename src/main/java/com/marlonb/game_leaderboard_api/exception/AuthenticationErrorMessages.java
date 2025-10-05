package com.marlonb.game_leaderboard_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationErrorMessages {

    AUTHENTICATION_MAIN_MESSAGE("Authentication failed"),
    EXPIRED_TOKEN("Token has expired. Please login again."),
    INVALID_TOKEN("Invalid authentication token."),
    FULL_AUTHENTICATION("Authentication required. Please provide a valid token.");

    private final String errorMessage;
}
