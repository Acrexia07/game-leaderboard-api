package com.marlonb.game_leaderboard_api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerResponseDto(
   Long id,
   UUID uuid,
   String username,
   Integer scores,
   LocalDateTime timestamp
) {}
