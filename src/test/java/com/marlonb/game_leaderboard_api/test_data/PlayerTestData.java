package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.PlayerUpdateDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerTestData {

    static final UUID playerUID = UUID.randomUUID();

    public static PlayerEntity samplePlayerData () {

        var samplePlayerData = new PlayerEntity();

        samplePlayerData.setId(1L);
        samplePlayerData.setUuid(playerUID);
        samplePlayerData.setPlayerName("player1");
        samplePlayerData.setScores(56700);
        samplePlayerData.setTimestamp(LocalDateTime.now());
        return samplePlayerData;
    }

    public static PlayerEntity samplePlayerDataAfterUpdate () {

        var samplePlayerDataAfterUpdate = new PlayerEntity();

        samplePlayerDataAfterUpdate.setId(1L);
        samplePlayerDataAfterUpdate.setUuid(samplePlayerData().getUuid());
        samplePlayerDataAfterUpdate.setPlayerName("player1");
        samplePlayerDataAfterUpdate.setScores(60000);
        samplePlayerDataAfterUpdate.setTimestamp(LocalDateTime.now());
        return samplePlayerDataAfterUpdate;
    }

    public static PlayerResponseDto samplePlayerResponse (PlayerEntity samplePlayerData) {

        return new PlayerResponseDto(
                samplePlayerData.getId(),
                samplePlayerData.getUuid(),
                samplePlayerData.getPlayerName(),
                samplePlayerData.getScores(),
                samplePlayerData.getTimestamp()
        );
    }

    public static PlayerUpdateDto samplePlayerUpdate () {

        return new PlayerUpdateDto(
                samplePlayerData().getPlayerName(),
                60000,
                samplePlayerData().getTimestamp()
        );
    }

    public static PlayerRequestDto samplePlayerRequest () {

        return new PlayerRequestDto(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getScores(),
                samplePlayerData().getTimestamp()
        );
    }
}
