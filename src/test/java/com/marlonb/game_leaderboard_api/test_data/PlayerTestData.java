package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerInfo;
import com.marlonb.game_leaderboard_api.model.PlayerInfoRequest;
import com.marlonb.game_leaderboard_api.model.PlayerInfoResponse;
import com.marlonb.game_leaderboard_api.model.PlayerInfoUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerTestData {

    public static PlayerInfo samplePlayerData () {

        var samplePlayerData = new PlayerInfo();
        samplePlayerData.setId(1L);
        samplePlayerData.setUuid(UUID.randomUUID());
        samplePlayerData.setPlayerName("player1");
        samplePlayerData.setScores(56700);
        samplePlayerData.setTimestamp(LocalDateTime.now());
        return samplePlayerData;
    }

    public static PlayerInfoResponse samplePlayerResponse () {

        return new PlayerInfoResponse (
                samplePlayerData().getId(),
                samplePlayerData().getUuid(),
                samplePlayerData().getPlayerName(),
                samplePlayerData().getScores(),
                samplePlayerData().getTimestamp()
        );
    }

    public static PlayerInfoUpdate samplePlayerUpdate () {

        return new PlayerInfoUpdate (
                samplePlayerData().getPlayerName(),
                60000,
                samplePlayerData().getTimestamp()
        );
    }

    public static PlayerInfoRequest samplePlayerRequest () {

        return new PlayerInfoRequest(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getScores(),
                samplePlayerData().getTimestamp()
        );
    }
}
