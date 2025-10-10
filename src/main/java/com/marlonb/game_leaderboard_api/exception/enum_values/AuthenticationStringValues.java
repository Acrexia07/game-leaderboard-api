package com.marlonb.game_leaderboard_api.exception.enum_values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationStringValues {

    EXPIRED("expired"),
    INVALID("Invalid"),
    FULL_AUTHENTICATION_VALUE("Full authentication");

    private final String expectedValueString;
}
