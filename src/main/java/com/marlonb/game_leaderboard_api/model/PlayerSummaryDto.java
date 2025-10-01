package com.marlonb.game_leaderboard_api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerSummaryDto(
        String playerName,
        UUID uuid,
        Integer scores,
        Integer gameRank
) {}
