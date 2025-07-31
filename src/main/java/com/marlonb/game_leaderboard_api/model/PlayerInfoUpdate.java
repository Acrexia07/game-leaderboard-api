package com.marlonb.game_leaderboard_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfoUpdate {

    private String playerName;

    private int scores;

    private LocalDateTime timeStamp;
}
