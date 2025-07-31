package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PlayerServiceUnitTests {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerInfoMapper playerMapper;

    @InjectMocks
    private PlayerService playerService;


    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should create player successfully")
        void shouldCreatePlayerSuccessfully () {

            // Arrange
            PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
            PlayerResponseDto expectedResponse = PlayerTestData.samplePlayerResponse(testPlayer);
            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();


            when(playerMapper.toEntity(any(PlayerRequestDto.class)))
                    .thenReturn(testPlayer);

            when(playerRepository.save(any(PlayerEntity.class)))
                    .thenReturn(testPlayer);

            when(playerMapper.toResponse(any(PlayerEntity.class)))
                    .thenReturn(expectedResponse);

            // Act
            PlayerResponseDto actualResponse = playerService.savePlayerData(testPlayerRequest);

            // Assert
            assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
        }
    }
}
