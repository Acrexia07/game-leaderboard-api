package com.marlonb.game_leaderboard_api.model.user;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;

import java.time.LocalDateTime;

public record UserResponseDto(
   long id,
   String username,
   String password,
   LocalDateTime createdAt
) {}
