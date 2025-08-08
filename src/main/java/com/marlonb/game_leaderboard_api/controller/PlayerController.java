package com.marlonb.game_leaderboard_api.controller;

import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import com.marlonb.game_leaderboard_api.utils.ApiMessageResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/players")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> createPlayerResource (@Valid @RequestBody
                                                                   PlayerRequestDto playerRequest) {

        PlayerResponseDto playerResponseForCreate = playerService.savePlayerData(playerRequest);
        URI location = URI.create("/api/players" + playerResponseForCreate.id());

        return ResponseEntity.created(location).body(new ApiMessageResponseDto<>
                                                    ("Player created successfully!",
                                                     playerResponseForCreate));
    }
}
