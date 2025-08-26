package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.*;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.marlonb.game_leaderboard_api.service.ServiceErrorMessages.*;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerInfoMapper playerMapper;

    // CREATE: Add new player data
    @Transactional
    public PlayerResponseDto savePlayerData (@Valid @RequestBody
                                                    PlayerRequestDto createRequest) {

        PlayerEntity createPlayer = playerMapper.toEntity(createRequest);

        if(playerRepository.existsByPlayerName(createPlayer.getPlayerName())) {
            throw new DuplicateResourceFoundException
                    (String.format(createPlayer.getPlayerName(), DUPLICATE_PLAYER_NAME_FOUND));
        }

        PlayerEntity savedPlayer = playerRepository.save(createPlayer);
        return playerMapper.toResponse(savedPlayer);
    }

    // READ: Retrieve top 3 players
    @Transactional(readOnly = true)
    public List<PlayerResponseDto> retrieveTop3Players () {

        return playerRepository.findTop3PlayerByOrderByScoresDescAndTimestampAsc()
                               .stream()
                               .map(playerMapper::toResponse)
                               .toList();
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

        PlayerEntity foundPlayer = findPlayerId(id);

        if(playerRepository.existsByPlayerName(playerUpdateDto.getPlayerName()) &&
                !foundPlayer.getPlayerName().equalsIgnoreCase(playerUpdateDto.getPlayerName())) {
            throw new DuplicateResourceFoundException
                    (String.format(playerUpdateDto.getPlayerName(), DUPLICATE_PLAYER_NAME_FOUND));
        }

        playerMapper.toUpdateFromEntity(foundPlayer, playerUpdateDto);
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
                                            (String.format("This player id '%d' does not exist!", id)));
    }
}
