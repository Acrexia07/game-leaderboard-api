package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.PlayerUpdateDto;
import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player2TestData {

    private static final PlayerEntity BASE_PLAYER_DATA;

    private static final Long PLAYER_ID = 2L;
    private static final UUID PLAYER_UUID = UUID.randomUUID();
    private static final String PLAYER_NAME = "Player2";
    private static final Integer PLAYER_SCORE = 76800;
    private static final Integer PLAYER_RANK = 3;
    private static final LocalDateTime PLAYER_TIMESTAMP = LocalDateTime.of
                                                          (2025, 2, 14, 0, 0);
    private static final UserEntity PLAYER_USER_ACCOUNT = User1TestData.sampleUser1Data();

    private static final Integer PLAYER_UPDATED_SCORE = 7200;

    static {
        BASE_PLAYER_DATA = new PlayerEntity();
        BASE_PLAYER_DATA.setId(PLAYER_ID);
        BASE_PLAYER_DATA.setUuid(PLAYER_UUID);
        BASE_PLAYER_DATA.setPlayerName(PLAYER_NAME);
        BASE_PLAYER_DATA.setScores(PLAYER_SCORE);
        BASE_PLAYER_DATA.setGameRank(PLAYER_RANK);
        BASE_PLAYER_DATA.setTimestamp(PLAYER_TIMESTAMP);
        BASE_PLAYER_DATA.setUser(PLAYER_USER_ACCOUNT);
    }

    public static PlayerEntity samplePlayerData2 () {
        return BASE_PLAYER_DATA;
    }

    public static PlayerResponseDto samplePlayerResponse2 (PlayerEntity samplePlayer2Data) {

        return new PlayerResponseDto(
                samplePlayer2Data.getId(),
                samplePlayer2Data.getUuid(),
                samplePlayer2Data.getPlayerName(),
                samplePlayer2Data.getScores(),
                samplePlayer2Data.getGameRank(),
                samplePlayer2Data.getTimestamp(),
                samplePlayer2Data.getUser().getId()
        );
    }

    public static PlayerUpdateDto samplePlayerUpdate2 () {

        return new PlayerUpdateDto(
                samplePlayerData2().getPlayerName(),
                PLAYER_UPDATED_SCORE
        );
    }

    public static PlayerEntity sampleIncompletePlayer2Data () {
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
