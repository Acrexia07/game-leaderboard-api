package com.marlonb.game_leaderboard_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    INTERNAL_SERVER_ERROR_MESSAGE("Internal server issue(s) found!"),
    DUPLICATE_RESOURCE_FOUND_MESSAGE("Resource duplication occurred!"),
    RESOURCE_NOT_FOUND_MESSAGE("Resource not found!"),
    JSON_ERROR_MESSAGE("Malformed JSON structure"),
    FORMAT_REQUEST_ERROR_MESSAGE("Invalid request format"),
    HTTP_MESSAGE_NOT_READABLE_ERROR_MESSAGE("Unable to process request body"),
    BAD_CREDENTIALS_MESSAGE("Bad credentials found!"),
    ACCESS_DENIED_MESSAGE("Forbidden access – insufficient permissions."),
    VALIDATION_ERROR_MESSAGE("Validation error(s) found!");

    private final String errorMessage;
}
