package com.marlonb.game_leaderboard_api.exception.enum_values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseKey {

    SERVER_KEY_VALUE("server"),
    PLAYER_KEY_VALUE("player"),
    CREDENTIALS_KEY_VALUE("credentials"),
    ERROR_KEY_VALUE("error"),
    ROLE_KEY_VALUE("role"),
    JSON_KEY_VALUE("json"),
    REQUEST_KEY_VALUE("request"),
    USER_KEY_VALUE("user"),
    RESOURCE_KEY_VALUE("resource");

    private final String keyValue;
}
