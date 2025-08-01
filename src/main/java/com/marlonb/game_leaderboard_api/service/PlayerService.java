package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
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

    public PlayerService(PlayerRepository playerRepository,
                         PlayerInfoMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    // CREATE: Add new player data
    @Transactional
    public PlayerResponseDto savePlayerData (@Valid @RequestBody
                                                    PlayerRequestDto createRequest) {

        PlayerEntity createPlayer = playerMapper.toEntity(createRequest);
        playerRepository.save(createPlayer);
        return playerMapper.toResponse(createPlayer);
    }

    // READ: Retrieve all players data
    @Transactional(readOnly = true)
    public List<PlayerResponseDto> retrieveAllPlayersData () {

        List<PlayerEntity> listOfPlayers = playerRepository.findAll();

        return listOfPlayers.stream()
                            .map(playerMapper::toResponse)
                            .toList();
    }

}
