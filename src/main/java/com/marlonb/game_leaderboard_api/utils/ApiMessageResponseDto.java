package com.marlonb.game_leaderboard_api.utils;

public record ApiMessageResponseDto<T> (
   String apiMessage,
   T response
) {}
