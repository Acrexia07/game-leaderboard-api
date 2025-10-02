package com.marlonb.game_leaderboard_api.model.user;

import com.marlonb.game_leaderboard_api.model.PlayerSummaryDto;

import java.time.LocalDateTime;

public record UserSummaryDto(
        String username,
        UserRoles role,
        LocalDateTime createdAt,
        PlayerSummaryDto playerAccount
) {}
