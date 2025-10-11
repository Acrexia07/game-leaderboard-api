package com.marlonb.game_leaderboard_api.controller;

import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.PlayerSummaryDto;
import com.marlonb.game_leaderboard_api.model.PlayerUpdateDto;
import com.marlonb.game_leaderboard_api.model.user.UserPrincipal;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.marlonb.game_leaderboard_api.controller.enum_values.PlayerApiSuccessfulMessages.*;
import static com.marlonb.game_leaderboard_api.exception.enum_values.ErrorMessages.*;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/players")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> createPlayerResource
                                                                (@Valid @RequestBody PlayerRequestDto playerRequest,
                                                                                     Authentication authentication) {
        String baseUrl = "/api/players/";

        // Save new player data and build a Location URI for the created resource
        PlayerResponseDto playerResponseForCreate = playerService.savePlayerData(playerRequest, authentication);
        URI location = URI.create(baseUrl + playerResponseForCreate.id());

        return ResponseEntity.created(location).body(new ApiMessageResponseDto<>
                                                    (PLAYER_CREATE_API_MESSAGE.getApiMessage(),
                                                     playerResponseForCreate));
    }

    @GetMapping("/players")
    public ResponseEntity<ApiMessageResponseDto<List<PlayerResponseDto>>> retrieveAllPlayerResources () {

        List<PlayerResponseDto> listOfPlayers = playerService.retrieveAllPlayersData();

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                       (PLAYER_READ_ALL_API_MESSAGE.getApiMessage(),
                                        listOfPlayers));
    }

    @GetMapping("/players/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> retrieveSpecificPlayerResource
                                                    (@PathVariable long id) {

        PlayerResponseDto foundResponse = playerService.retrieveSpecificPlayerData(id);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        (PLAYER_READ_BY_ID_API_MESSAGE.getApiMessage(),
                                         foundResponse));
    }

    @GetMapping("/leaderboards")
    public ResponseEntity<ApiMessageResponseDto<List<PlayerResponseDto>>> retrieveTop3Players () {

        List<PlayerResponseDto> listOfTopPlayers = playerService.retrieveTop3Players();

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                       (PLAYER_LEADERBOARD_API_MESSAGE.getApiMessage(),
                                        listOfTopPlayers));
    }

    @PutMapping("/players/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> updateSpecificPlayerResource
            (@PathVariable long id, @Valid @RequestBody PlayerUpdateDto updateDto) {

        PlayerResponseDto response = playerService.updateSpecificPlayerData(id, updateDto);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        (PLAYER_UPDATE_BY_ID_API_MESSAGE.getApiMessage(),
                                         response));
    }

    /* --- Self endpoints --- */
    @GetMapping("/players/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiMessageResponseDto<PlayerSummaryDto>> retrievePlayerProfileResource (Authentication auth) {

        // Extract the logged-in user's Player ID from the security context (UserPrincipal)
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Long playerId = principal.getPlayerId();

        if (playerId == null) {
           throw new ResourceNotFoundException(PLAYER_NOT_FOUND_ERROR_MESSAGE.getErrorMessage());
        }

        PlayerSummaryDto profileResponse = playerService.getPlayerProfile(playerId);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        (PLAYER_READ_PROFILE_API_MESSAGE.getApiMessage(),
                                        profileResponse));
    }

    @PutMapping("/players/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiMessageResponseDto<PlayerSummaryDto>> updatePlayerProfile (Authentication authentication,
                                                                                        @Valid @RequestBody
                                                                                        PlayerUpdateDto playerUpdate) {

        // Extract the logged-in user's Player ID from the security context (UserPrincipal)
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long playerId = principal.getPlayerId();

        if (playerId == null) {
            throw new ResourceNotFoundException("Player account not created yet for this user");
        }

       PlayerSummaryDto profileResponse = playerService.updatePlayerProfile(playerId, playerUpdate);

       return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                       (PLAYER_UPDATE_PROFILE_API_MESSAGE.getApiMessage(),
                                        profileResponse));
    }
}
