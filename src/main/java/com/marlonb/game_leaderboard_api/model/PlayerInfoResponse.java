package com.marlonb.game_leaderboard_api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerInfoResponse(
   Long id,
   UUID uuid,
   String username,
   int scores,
   LocalDateTime timestamp
) {}
