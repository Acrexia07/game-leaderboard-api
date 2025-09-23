package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player3TestData {

    private static final PlayerEntity BASE_PLAYER_DATA;

    private static final Long PLAYER_ID = 3L;
    private static final UUID PLAYER_UUID = UUID.randomUUID();
    private static final String PLAYER_NAME = "Player1";
    private static final Integer PLAYER_SCORE = 99900;
    private static final LocalDateTime PLAYER_TIMESTAMP = LocalDateTime.of
                                                              (2025, 1, 1, 0, 0);
    private static final UserEntity PLAYER_USER_ACCOUNT = User1TestData.sampleUser1Data();

    static {
        BASE_PLAYER_DATA = new PlayerEntity();
        BASE_PLAYER_DATA.setId(PLAYER_ID);
        BASE_PLAYER_DATA.setUuid(PLAYER_UUID);
        BASE_PLAYER_DATA.setPlayerName(PLAYER_NAME);
        BASE_PLAYER_DATA.setScores(PLAYER_SCORE);
        BASE_PLAYER_DATA.setTimestamp(PLAYER_TIMESTAMP);
        BASE_PLAYER_DATA.setUser(PLAYER_USER_ACCOUNT);
    }

    public static PlayerEntity samplePlayer3Data () {
        return BASE_PLAYER_DATA;
    }

    public static PlayerResponseDto samplePlayer3Response (PlayerEntity samplePlayer3Data) {

        return new PlayerResponseDto(
                samplePlayer3Data.getId(),
                samplePlayer3Data.getUuid(),
                samplePlayer3Data.getPlayerName(),
                samplePlayer3Data.getScores(),
                samplePlayer3Data.getTimestamp(),
                samplePlayer3Data.getUser().getId()
        );
    }

    public static PlayerEntity sampleIncompletePlayer3Data () {
        PlayerEntity player = new PlayerEntity();
        player.setId(null);
        player.setUuid(PLAYER_UUID);
        player.setPlayerName(PLAYER_NAME);
        player.setScores(PLAYER_SCORE);
        player.setTimestamp(PLAYER_TIMESTAMP);
        player.setUser(null);
        return player;
    }
}
