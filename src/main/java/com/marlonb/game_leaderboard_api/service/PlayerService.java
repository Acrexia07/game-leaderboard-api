package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PlayerService {

    private static PlayerRepository playerRepository;
    private static PlayerInfoMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository,
                         PlayerInfoMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public static PlayerResponseDto savePlayerData (@Valid @RequestBody
                                                    PlayerRequestDto createRequest) {

        PlayerEntity createPlayer = playerMapper.toEntity(createRequest);
        playerRepository.save(createPlayer);
        return playerMapper.toResponse(createPlayer);
    }
}
