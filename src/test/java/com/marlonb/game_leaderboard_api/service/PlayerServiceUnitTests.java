package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import com.marlonb.game_leaderboard_api.test_data.Player2TestData;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
            assertThat(testPlayer.getUuid()).isEqualTo(actualResponse.uuid());
        }

        @Test
        @DisplayName("Should retrieve all players data successfully")
        void shouldRetrieveAllPlayersDataSuccessfully () {

            // Arrange
            PlayerEntity testPlayer1 = PlayerTestData.samplePlayerData();
            PlayerEntity testPlayer2 = Player2TestData.samplePlayerData2();

            PlayerResponseDto playerResponse1 = PlayerTestData.samplePlayerResponse(testPlayer1);
            PlayerResponseDto playerResponse2 = Player2TestData.samplePlayerResponse2(testPlayer2);

            List<PlayerEntity> listOfPlayerEntities = List.of(testPlayer1, testPlayer2);
            List<PlayerResponseDto> expectedResponse = List.of(playerResponse1, playerResponse2);

            when(playerRepository.findAll()).thenReturn(listOfPlayerEntities);

            when(playerMapper.toResponse(testPlayer1)).thenReturn(playerResponse1);
            when(playerMapper.toResponse(testPlayer2)).thenReturn(playerResponse2);

            // Act
            List<PlayerResponseDto> actualResponse = playerService.retrieveAllPlayersData();

            // Assert
            assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
            assertThat(testPlayer1.getUuid()).usingRecursiveAssertion().isEqualTo(actualResponse.getFirst().uuid());
            assertThat(testPlayer2.getUuid()).usingRecursiveAssertion().isEqualTo(actualResponse.getLast().uuid());
        }
    }
}
