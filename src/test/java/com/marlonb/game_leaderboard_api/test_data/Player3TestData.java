package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player3TestData {

    public static PlayerEntity samplePlayer3Data () {

        var samplePlayer3Data = new PlayerEntity();
        final UUID player3UID = UUID.randomUUID();

        samplePlayer3Data.setId(3L);
        samplePlayer3Data.setUuid(player3UID);
        samplePlayer3Data.setPlayerName("apollo35");
        samplePlayer3Data.setScores(99900);
        samplePlayer3Data.setTimestamp(LocalDateTime.now());

        return  samplePlayer3Data;
    }

    public static PlayerResponseDto samplePlayer3Response (PlayerEntity samplePlayer3Data) {

        return new PlayerResponseDto(
                samplePlayer3Data.getId(),
                samplePlayer3Data.getUuid(),
                samplePlayer3Data.getPlayerName(),
                samplePlayer3Data.getScores(),
                samplePlayer3Data.getTimestamp()
        );
    }

}
