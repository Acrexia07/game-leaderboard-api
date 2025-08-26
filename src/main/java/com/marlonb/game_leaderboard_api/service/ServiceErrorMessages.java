package com.marlonb.game_leaderboard_api.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceErrorMessages {

    DUPLICATE_USERNAME_FOUND("Username '%s' already exist!"),
    DUPLICATE_PLAYER_NAME_FOUND("Player name '%s' already exist!");

    private final String errorMessage;
}
