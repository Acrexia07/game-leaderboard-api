package com.marlonb.game_leaderboard_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HttpClientErrorMessage {
    UNAUTHORIZED("You are not authorized. Please log in"),
    FORBIDDEN("You don’t have permission to access this resource"),
    NOT_FOUND("The requested resource was not found"),
    BAD_REQUEST("Invalid request. Please check your input"),
    VALIDATION_ISSUE("Validation failed"),
    UNREADABLE_BODY("Unreadable request body"),
    DEFAULT("Something went wrong. Please try again");

    private final String errorMessage;

    public static String fromStatus (HttpStatus status){

        return switch (status){
            case UNAUTHORIZED -> UNAUTHORIZED.getErrorMessage();
            case FORBIDDEN -> FORBIDDEN.getErrorMessage();
            case NOT_FOUND -> NOT_FOUND.getErrorMessage();
            case BAD_REQUEST -> BAD_REQUEST.getErrorMessage();
            default -> DEFAULT.getErrorMessage();
        };
    }
}
