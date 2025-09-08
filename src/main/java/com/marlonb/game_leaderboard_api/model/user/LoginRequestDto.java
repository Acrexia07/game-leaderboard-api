package com.marlonb.game_leaderboard_api.model.user;

public record LoginRequestDto(
   String username,
   String password
) {}
