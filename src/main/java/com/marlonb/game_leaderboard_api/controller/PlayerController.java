package com.marlonb.game_leaderboard_api.controller;

import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.PlayerUpdateDto;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import com.marlonb.game_leaderboard_api.utils.ApiMessageResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
        URI location = URI.create("/api/players/" + playerResponseForCreate.id());

        return ResponseEntity.created(location).body(new ApiMessageResponseDto<>
                                                    ("Player created successfully!",
                                                     playerResponseForCreate));
    }

    @GetMapping("/players")
    public ResponseEntity<ApiMessageResponseDto<List<PlayerResponseDto>>> retrieveAllPlayerResources () {

        List<PlayerResponseDto> listOfPlayers = playerService.retrieveAllPlayersData();

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                       ("Retrieved all players successfully!",
                                        listOfPlayers));
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> retrieveSpecificPlayerResource
                                                    (@PathVariable long id) {

        PlayerResponseDto foundResponse = playerService.retrieveSpecificPlayerData(id);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Specific player data retrieved successfully!", foundResponse));
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> updateSpecificPlayerResource
            (@PathVariable long id, @Valid @RequestBody PlayerUpdateDto updateDto) {

        PlayerResponseDto response = playerService.updateSpecificPlayerData(id, updateDto);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Specific player updated successfully!",
                                         response));
    }
}
