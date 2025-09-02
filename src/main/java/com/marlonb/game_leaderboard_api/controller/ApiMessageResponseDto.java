package com.marlonb.game_leaderboard_api.controller;

public record ApiMessageResponseDto<T> (
   String apiMessage,
   T response
) {}
