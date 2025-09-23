package com.marlonb.game_leaderboard_api.controller;

import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.PlayerSummaryDto;
import com.marlonb.game_leaderboard_api.model.PlayerUpdateDto;
import com.marlonb.game_leaderboard_api.model.user.UserPrincipal;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> createPlayerResource
                                                                (@Valid @RequestBody PlayerRequestDto playerRequest,
                                                                                     Authentication authentication) {

        // Save new player data and build a Location URI for the created resource
        PlayerResponseDto playerResponseForCreate = playerService.savePlayerData(playerRequest, authentication);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> retrieveSpecificPlayerResource
                                                    (@PathVariable long id) {

        PlayerResponseDto foundResponse = playerService.retrieveSpecificPlayerData(id);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Retrieved specific player successfully!", foundResponse));
    }

    @GetMapping("/leaderboards")
    public ResponseEntity<ApiMessageResponseDto<List<PlayerResponseDto>>> retrieveTop3Players () {

        List<PlayerResponseDto> listOfTopPlayers = playerService.retrieveTop3Players();

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                       ("Retrieve top 3 players successfully!",
                                        listOfTopPlayers));
    }

    @PutMapping("/players/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiMessageResponseDto<PlayerResponseDto>> updateSpecificPlayerResource
            (@PathVariable long id, @Valid @RequestBody PlayerUpdateDto updateDto) {

        PlayerResponseDto response = playerService.updateSpecificPlayerData(id, updateDto);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Specific player updated successfully!",
                                         response));
    }

    @DeleteMapping("/players/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.player.id")
    public ResponseEntity<Void> deleteSpecificPlayerResource (@PathVariable long id) {

        playerService.deleteSpecificPlayerData(id);
        return ResponseEntity.noContent().build();
    }

    /* --- User endpoints --- */
    @GetMapping("/players/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiMessageResponseDto<PlayerSummaryDto>> retrievePlayerProfileResource (Authentication auth) {

        // Extract the logged-in user's Player ID from the security context (UserPrincipal)
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Long playerId = principal.getPlayerId();

        if (playerId == null) {
           throw new ResourceNotFoundException("Player account not created yet for this user");
        }

        PlayerSummaryDto profileResponse = playerService.getPlayerProfile(playerId);

        return ResponseEntity.ok().body(new ApiMessageResponseDto<>
                                        ("Retrieve player profile successfully!",
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
                                       ("Update player profile successfully!",
                                        profileResponse));
    }
}
