package com.marlonb.game_leaderboard_api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerResponseDto(
   Long id,
   UUID uuid,
   String playerName,
   Integer scores,
   LocalDateTime timestamp,
   Long userId
) {}
