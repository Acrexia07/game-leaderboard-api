package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.PlayerUpdateDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player2TestData {

    public static PlayerEntity samplePlayerData2 () {

        var samplePlayerData2 = new PlayerEntity();
        final UUID player2UID = UUID.randomUUID();

        samplePlayerData2.setId(2L);
        samplePlayerData2.setUuid(player2UID);
        samplePlayerData2.setPlayerName("sonicChad6");
        samplePlayerData2.setScores(62300);
        samplePlayerData2.setTimestamp(LocalDateTime.now().minusMinutes(30));

        return  samplePlayerData2;
    }

    public static PlayerResponseDto samplePlayerResponse2 (PlayerEntity samplePlayer2Data) {

        return new PlayerResponseDto(
                samplePlayer2Data.getId(),
                samplePlayer2Data.getUuid(),
                samplePlayer2Data.getPlayerName(),
                samplePlayer2Data.getScores(),
                samplePlayer2Data.getTimestamp()
        );
    }

    public static PlayerUpdateDto samplePlayerUpdate2 () {

        return new PlayerUpdateDto(
                samplePlayerData2().getPlayerName(),
                7200
        );
    }

    public static PlayerRequestDto samplePlayerRequest2 () {

        return new PlayerRequestDto(
                samplePlayerData2().getPlayerName(),
                samplePlayerData2().getScores()
        );
    }
}
