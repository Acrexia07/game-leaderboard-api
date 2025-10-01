package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.*;
import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserPrincipal;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerTestData {

    private static final PlayerEntity BASE_PLAYER_DATA;

    /* --- RAW VALUES --- */
    private static final Long PLAYER_ID = 1L;
    private static final UUID PLAYER_UUID = UUID.randomUUID();
    private static final String PLAYER_NAME = "Player1";
    private static final Integer PLAYER_SCORE = 56700;
    private static final Integer PLAYER_RANK = 4;
    private static final LocalDateTime PLAYER_TIMESTAMP = LocalDateTime.of
                                                              (2025, 1, 20, 0, 0);
    private static final UserEntity PLAYER_USER_ACCOUNT = User1TestData.sampleUser1Data();

    /* --- UPDATED VALUES --- */
    private static final int UPDATED_PLAYER_SCORE = 60000;

    /* --- INVALID VALUES --- */
    private static final String INVALID_PLAYER_NAME = "user_12345694668";
    private static final PlayerEntity NULL_PLAYER = null;

    /* --- PLAYER DATA --- */
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

    public static PlayerEntity samplePlayerData () {
        return BASE_PLAYER_DATA;
    }

    public static PlayerEntity samplePlayerDataAfterUpdate () {
        BASE_PLAYER_DATA.setScores(UPDATED_PLAYER_SCORE);
        return BASE_PLAYER_DATA;
    }

    public static PlayerResponseDto samplePlayerResponse (PlayerEntity samplePlayerData) {

        return new PlayerResponseDto(
                samplePlayerData.getId(),
                samplePlayerData.getUuid(),
                samplePlayerData.getPlayerName(),
                samplePlayerData.getScores(),
                samplePlayerData.getGameRank(),
                samplePlayerData.getTimestamp(),
                samplePlayerData.getUser().getId()
        );
    }

    public static PlayerUpdateDto samplePlayerUpdate () {

        return new PlayerUpdateDto(
                samplePlayerData().getPlayerName(),
                UPDATED_PLAYER_SCORE
        );
    }

    public static PlayerResponseDto samplePlayerResponseAfterUpdate () {

        return new PlayerResponseDto(
                samplePlayerData().getId(),
                samplePlayerData().getUuid(),
                samplePlayerUpdate().getPlayerName(),
                samplePlayerUpdate().getScores(),
                samplePlayerData().getGameRank(),
                samplePlayerData().getTimestamp(),
                samplePlayerData().getUser().getId()
        );
    }

    public static PlayerRequestDto samplePlayerRequest () {

        return new PlayerRequestDto(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getScores()
        );
    }

    public static PlayerRequestDto sampleInvalidPlayerRequest () {

        return new PlayerRequestDto(
                INVALID_PLAYER_NAME,
                samplePlayerData().getScores()
        );
    }

    public static UserPrincipal sampleUserPrincipal () {
        UserEntity user = User1TestData.sampleUser1Data();
        user.setPlayer(BASE_PLAYER_DATA);
        return new UserPrincipal(user);
    }

    public static UserPrincipal sampleUserWithoutPlayerAccount () {
        UserEntity user = User1TestData.sampleUser1Data();
        user.setPlayer(NULL_PLAYER);
        return new UserPrincipal(user);
    }

    public static PlayerSummaryDto samplePlayerSummary () {

        return new PlayerSummaryDto(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getUuid(),
                samplePlayerData().getScores(),
                samplePlayerData().getGameRank()
        );
    }

    public static PlayerSummaryDto samplePlayerUpdateSummary () {

        return new PlayerSummaryDto(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getUuid(),
                samplePlayerUpdate().getScores(),
                samplePlayerData().getGameRank()
        );
    }

    /*--- PLAYER DATA FOR INTEGRATION TESTS --- */
    public static PlayerEntity samplePlayerDataWithoutID () {
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
