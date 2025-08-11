package com.marlonb.game_leaderboard_api.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        int httpStatusErrorCode,
        String generalErrorMessage,
        Map<String, List<String>> errorList
) {}
