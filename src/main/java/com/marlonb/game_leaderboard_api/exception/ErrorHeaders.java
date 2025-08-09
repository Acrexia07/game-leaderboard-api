package com.marlonb.game_leaderboard_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorHeaders {
    INTERNAL_SERVER_ERROR_MESSAGE("Internal server issue(s) found!"),
    DUPLICATE_RESOURCE_FOUND_MESSAGE("Resource duplication occurred!"),
    RESOURCE_NOT_FOUND_MESSAGE("Resource not found!"),
    VALIDATION_ERROR_MESSAGE("Validation error(s) found!");

    private final String errorMessage;
}
