package com.marlonb.game_leaderboard_api.model.user;

public record JwtResponseDto(
   String token,
   String username
) {}
