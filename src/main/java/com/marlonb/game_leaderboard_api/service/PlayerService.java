package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.*;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerInfoMapper playerMapper;


    public PlayerService (PlayerRepository playerRepository,
                          PlayerInfoMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    // CREATE: Add new player data
    @Transactional
    public PlayerResponseDto savePlayerData (@Valid @RequestBody
                                                    PlayerRequestDto createRequest) {

        final String DUPLICATE_RESOURCE_FOUND = "Player name '%s' already exist!";

        PlayerEntity createPlayer = playerMapper.toEntity(createRequest);

        if(playerRepository.existsByPlayerName(createPlayer.getPlayerName())) {
            throw new DuplicateResourceFoundException
                    (String.format(DUPLICATE_RESOURCE_FOUND, createPlayer.getPlayerName()));
        }

        PlayerEntity savedPlayer = playerRepository.save(createPlayer);
        return playerMapper.toResponse(savedPlayer);
    }

    // READ: Retrieve all players data
    @Transactional(readOnly = true)
    public List<PlayerResponseDto> retrieveAllPlayersData () {

        List<PlayerEntity> listOfPlayers = playerRepository.findAll();

        return listOfPlayers.stream()
                            .map(playerMapper::toResponse)
                            .toList();
    }

    // READ: Retrieve specific player data
    @Transactional(readOnly = true)
    public PlayerResponseDto retrieveSpecificPlayerData (long id) {

        PlayerEntity foundPlayer = findPlayerId(id);
        return playerMapper.toResponse(foundPlayer);
    }

    // UPDATE: Update specific player data
    @Transactional
    public PlayerResponseDto updateSpecificPlayerData (long id, PlayerUpdateDto playerUpdateDto) {

        final String DUPLICATE_RESOURCE_FOUND = "Player name '%s' already exist!";

        PlayerEntity foundPlayer = findPlayerId(id);
        playerMapper.toUpdateFromEntity(foundPlayer, playerUpdateDto);

        if(playerRepository.existsByPlayerName(playerUpdateDto.getPlayerName()) &&
                !foundPlayer.getPlayerName().equalsIgnoreCase(playerUpdateDto.getPlayerName())) {
            throw new DuplicateResourceFoundException
                    (String.format(DUPLICATE_RESOURCE_FOUND, playerUpdateDto.getPlayerName()));
        }

        PlayerEntity savedUpdatedPlayer = playerRepository.save(foundPlayer);

        return playerMapper.toResponse(savedUpdatedPlayer);
    }

    // DELETE: remove specific player data
    @Transactional
    public void deleteSpecificPlayerData (long id) {

        findPlayerId(id);
        playerRepository.deleteById(id);
    }

    // Helper
    public PlayerEntity findPlayerId (long id) {

        return playerRepository.findById(id)
                               .orElseThrow(() -> new ResourceNotFoundException
                                            (String.format("This player Id '%d' does not exist!", id)));
    }
}
