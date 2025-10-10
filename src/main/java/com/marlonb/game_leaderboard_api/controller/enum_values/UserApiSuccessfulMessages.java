package com.marlonb.game_leaderboard_api.controller.enum_values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserApiSuccessfulMessages {

    USER_CREATE_API_MESSAGE("User created successfully!"),
    USER_LOGIN_API_MESSAGE("User login successfully!"),
    USER_CREATE_ADMIN_MESSAGE("Admin created successfully!"),
    USER_READ_ALL_API_MESSAGE("Retrieved all users successfully!"),
    USER_READ_BY_ID_API_MESSAGE("Retrieved specific user successfully!"),
    USER_UPDATE_BY_ID_API_MESSAGE("Updated specific user successfully!"),
    USER_READ_PROFILE_API_MESSAGE("Retrieved user profile successfully!"),
    USER_UPDATE_PROFILE_API_MESSAGE("Update user profile successfully!");

    private final String apiMessage;
}
