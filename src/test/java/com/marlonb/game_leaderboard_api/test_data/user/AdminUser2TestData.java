package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.PlayerSummaryDto;
import com.marlonb.game_leaderboard_api.model.user.*;

import java.time.LocalDateTime;

import static com.marlonb.game_leaderboard_api.test_data.Player2TestData.samplePlayerData2;

public class AdminUser2TestData {

    public static UserEntity sampleAdminUser2Data () {

        UserEntity admin2 = new UserEntity();
        admin2.setId(4L);
        admin2.setUsername("admin2");
        admin2.setPassword("Admin@abc1");
        admin2.setRole(UserRoles.ADMIN);
        admin2.setCreatedAt(LocalDateTime.now());
        return admin2;
    }

    public static UserEntity sampleAdminUser2DataAfterUpdate () {

        UserEntity updatedAdmin2 = new UserEntity();
        updatedAdmin2.setId(4L);
        updatedAdmin2.setUsername("admin1");
        updatedAdmin2.setPassword("Admin#abc1");
        updatedAdmin2.setRole(UserRoles.ADMIN);
        updatedAdmin2.setCreatedAt(LocalDateTime.now());
        return updatedAdmin2;
    }

    public static PlayerSummaryDto playerSummaryDto () {

        return new PlayerSummaryDto(
                samplePlayerData2().getPlayerName(),
                samplePlayerData2().getUuid(),
                samplePlayerData2().getScores()
        );
    }

    public static UserResponseDto sampleAdminUser2Response () {

        return new UserResponseDto(
                sampleAdminUser2Data().getId(),
                sampleAdminUser2Data().getUsername(),
                sampleAdminUser2Data().getPassword(),
                sampleAdminUser2Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    public static AdminUserRequestDto sampleAdminUser2Request () {

        return new AdminUserRequestDto(
                sampleAdminUser2Data().getUsername(),
                sampleAdminUser2Data().getPassword(),
                sampleAdminUser2Data().getRole()
        );
    }

    public static AdminUserUpdateDto sampleAdminUser2Update () {

        return new AdminUserUpdateDto(
                sampleAdminUser2Data().getUsername(),
                "Admin#def2",
                sampleAdminUser2Data().getRole()
        );
    }
}
