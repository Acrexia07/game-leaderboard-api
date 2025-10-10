package com.marlonb.game_leaderboard_api.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerApiSuccessfulMessages {

    PLAYER_CREATE_API_MESSAGE("Player created successfully!"),
    PLAYER_READ_ALL_API_MESSAGE("Retrieved all players successfully!"),
    PLAYER_READ_BY_ID_API_MESSAGE("Retrieved specific player successfully!"),
    PLAYER_LEADERBOARD_API_MESSAGE("Retrieve top 3 players successfully!"),
    PLAYER_UPDATE_BY_ID_API_MESSAGE("Specific player updated successfully!"),
    PLAYER_READ_PROFILE_API_MESSAGE("Retrieve player profile successfully!"),
    PLAYER_UPDATE_PROFILE_API_MESSAGE("Update player profile successfully!");

    private final String apiMessage;
}
