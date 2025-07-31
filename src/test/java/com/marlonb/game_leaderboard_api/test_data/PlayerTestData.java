package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerInfo;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerTestData {

    public static PlayerInfo samplePlayerData () {

        var samplePlayerData = new PlayerInfo();
        samplePlayerData.setId(1L);
        samplePlayerData.setUuid(UUID.randomUUID());
        samplePlayerData.setUsername("player1");
        samplePlayerData.setScores(56700);
        samplePlayerData.setTimeStamp(LocalDateTime.now());
        return samplePlayerData;
    }
}
