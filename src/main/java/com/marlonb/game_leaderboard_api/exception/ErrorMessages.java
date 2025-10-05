package com.marlonb.game_leaderboard_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    INTERNAL_SERVER_ERROR_MESSAGE("Internal server error"),
    DUPLICATE_RESOURCE_FOUND_MESSAGE("Duplicate resource"),
    RESOURCE_NOT_FOUND_MESSAGE("Resource not found"),
    JSON_ERROR_MESSAGE("Malformed JSON Syntax"),
    FORMAT_REQUEST_ERROR_MESSAGE("Invalid request"),
    HTTP_MESSAGE_NOT_READABLE_ERROR_MESSAGE("Unreadable request body"),
    BAD_CREDENTIALS_MESSAGE("Bad credentials"),
    ACCESS_DENIED_MESSAGE("Forbidden access"),
    DATA_INTEGRITY_ISSUE_MESSAGE("Data integration conflict"),
    PLAYER_CREATION_ERROR_MESSAGE("Player account not created yet for this user"),
    USER_ROLE_ERROR_MESSAGE("Invalid value. Accepts 'ADMIN' or 'USER' only"),
    VALIDATION_ERROR_MESSAGE("Validation error");

    private final String errorMessage;
}
