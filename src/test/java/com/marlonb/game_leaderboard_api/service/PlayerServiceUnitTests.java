package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceUnitTests {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerInfoMapper playerMapper;

    @InjectMocks
    private PlayerService playerService;



}
