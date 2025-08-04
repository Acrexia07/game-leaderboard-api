package com.marlonb.game_leaderboard_api.exception.custom;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException (String message) {
        super(message);
    }
}
