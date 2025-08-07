package com.marlonb.game_leaderboard_api.exception.custom;

public class DuplicateResourceFoundException extends RuntimeException{

    public DuplicateResourceFoundException (String message) {
        super(message);
    }
}
