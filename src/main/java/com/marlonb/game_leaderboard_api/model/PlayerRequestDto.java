package com.marlonb.game_leaderboard_api.model;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRequestDto {

    @NotBlank(message = "Player name is required!")
    @Size(max = 12, message = "Player name must not exceed 12 characters!")
    private String playerName;

    @NotNull(message = "Score is required!")
    @Min(value = 0, message = "Score must be positive number!")
    private Integer scores;
}
