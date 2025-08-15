package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player4TestData {

    public static PlayerEntity samplePlayer4Data () {

        var samplePlayer4Data = new PlayerEntity();
        final UUID player4UID = UUID.randomUUID();

        samplePlayer4Data.setId(3L);
        samplePlayer4Data.setUuid(player4UID);
        samplePlayer4Data.setPlayerName("apollo35");
        samplePlayer4Data.setScores(99900);
        samplePlayer4Data.setTimestamp(LocalDateTime.now());

        return  samplePlayer4Data;
    }

    public static PlayerResponseDto samplePlayer4Response (PlayerEntity samplePlayer4Data) {

        return new PlayerResponseDto(
                samplePlayer4Data.getId(),
                samplePlayer4Data.getUuid(),
                samplePlayer4Data.getPlayerName(),
                samplePlayer4Data.getScores(),
                samplePlayer4Data.getTimestamp()
        );
    }
}
