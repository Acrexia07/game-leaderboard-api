package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player4TestData {

    private static final PlayerEntity BASE_PLAYER4_DATA;

    /* --- RAW VALUES --- */
    private static final Long PLAYER_ID = 4L;
    private static final UUID PLAYER_UUID = UUID.randomUUID();
    private static final String PLAYER_NAME = "Player4";
    private static final Integer PLAYER_SCORE = 78700;
    private static final LocalDateTime PLAYER_TIMESTAMP = LocalDateTime.of
                                                              (2025, 1, 5, 0, 0);
    private static final UserEntity PLAYER_USER_ACCOUNT = User1TestData.sampleUser1Data();

    /* --- PLAYER DATA --- */
    static {
        BASE_PLAYER4_DATA = new PlayerEntity();
        BASE_PLAYER4_DATA.setId(PLAYER_ID);
        BASE_PLAYER4_DATA.setUuid(PLAYER_UUID);
        BASE_PLAYER4_DATA.setPlayerName(PLAYER_NAME);
        BASE_PLAYER4_DATA.setScores(PLAYER_SCORE);
        BASE_PLAYER4_DATA.setTimestamp(PLAYER_TIMESTAMP);
        BASE_PLAYER4_DATA.setUser(PLAYER_USER_ACCOUNT);
    }

    public static PlayerEntity sampleIncompletePlayer4Data () {
        return new PlayerEntity(
                null,
                PLAYER_UUID,
                PLAYER_NAME,
                PLAYER_SCORE,
                PLAYER_TIMESTAMP,
                null
        );
    }


}
