package com.marlonb.game_leaderboard_api.test_data;

import com.marlonb.game_leaderboard_api.model.*;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerTestData {

    private static final UUID playerUID = UUID.randomUUID();
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final PlayerEntity BASE_PLAYER_DATA;

    private static final int UPDATED_PLAYER_SCORE = 60000;

    static {
        BASE_PLAYER_DATA = new PlayerEntity();

        BASE_PLAYER_DATA.setId(1L);
        BASE_PLAYER_DATA.setUuid(playerUID);
        BASE_PLAYER_DATA.setPlayerName("player1");
        BASE_PLAYER_DATA.setScores(56700);
        BASE_PLAYER_DATA.setTimestamp(LocalDateTime.of(2025, 1, 1, 0, 0));
        BASE_PLAYER_DATA.setUser(User1TestData.sampleUser1Data());
    }

    public static PlayerEntity samplePlayerData () {
        return BASE_PLAYER_DATA;
    }

    public static PlayerEntity samplePlayerDataAfterUpdate () {

        var samplePlayerDataAfterUpdate = new PlayerEntity();

        samplePlayerDataAfterUpdate.setId(BASE_PLAYER_DATA.getId());
        samplePlayerDataAfterUpdate.setUuid(BASE_PLAYER_DATA.getUuid());
        samplePlayerDataAfterUpdate.setPlayerName(BASE_PLAYER_DATA.getPlayerName());
        samplePlayerDataAfterUpdate.setScores(UPDATED_PLAYER_SCORE);
        samplePlayerDataAfterUpdate.setTimestamp(BASE_PLAYER_DATA.getTimestamp());
        samplePlayerDataAfterUpdate.setUser(User1TestData.sampleUser1Data());
        return samplePlayerDataAfterUpdate;
    }

    public static PlayerResponseDto samplePlayerResponse (PlayerEntity samplePlayerData) {

        return new PlayerResponseDto(
                samplePlayerData.getId(),
                samplePlayerData.getUuid(),
                samplePlayerData.getPlayerName(),
                samplePlayerData.getScores(),
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
                "user_12345694668",
                samplePlayerData().getScores()
        );
    }
}
