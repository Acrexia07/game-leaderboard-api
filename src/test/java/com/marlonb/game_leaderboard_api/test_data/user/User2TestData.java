package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.PlayerSummaryDto;
import com.marlonb.game_leaderboard_api.model.user.*;

import static com.marlonb.game_leaderboard_api.test_data.Player2TestData.samplePlayerData2;

public class User2TestData {

    private static final UserEntity BASE_USER2_DATA;

    /* --- RAW VALUES --- */
    private static final Long USER2_ID = 2L;
    private static final String USER2_NAME = "User2";
    private static final String USER2_PASSWORD = "Test@123";
    private static final UserRoles USER2_ROLE = UserRoles.USER;

    /* --- UPDATED VALUES --- */
    private static final String UPDATE_USER2_PASSWORD = "User#789";

    static {
        BASE_USER2_DATA = new UserEntity();
        BASE_USER2_DATA.setId(USER2_ID);
        BASE_USER2_DATA.setUsername(USER2_NAME);
        BASE_USER2_DATA.setPassword(USER2_PASSWORD);
        BASE_USER2_DATA.setRole(USER2_ROLE);
    }

    public static UserEntity sampleUser2Data () {
        return BASE_USER2_DATA;
    }

    public static PlayerSummaryDto playerSummaryDto () {

        return new PlayerSummaryDto(
                samplePlayerData2().getPlayerName(),
                samplePlayerData2().getUuid(),
                samplePlayerData2().getScores()
        );
    }

    public static UserResponseDto sampleUser2Response () {

        return new UserResponseDto(
                sampleUser2Data().getId(),
                sampleUser2Data().getUsername(),
                sampleUser2Data().getPassword(),
                sampleUser2Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    public static UserUpdateDto sampleUser2Update () {

        return new UserUpdateDto(
                sampleUser2Data().getUsername(),
                UPDATE_USER2_PASSWORD
        );
    }

}
