package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import com.marlonb.game_leaderboard_api.test_data.Player2TestData;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @Nested
    class NegativeTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"", "Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do."})
        @DisplayName("Should fail when player name is not valid")
        void shouldFailWhenPlayerNameIsNotValid (String invalidPlayerNames) {

            // Arrange
            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();
            testPlayerRequest.setPlayerName(invalidPlayerNames);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            // Act
            Set<ConstraintViolation<PlayerRequestDto>> requestViolations =
                    validator.validate(testPlayerRequest);

            // Assert
            assertThat(requestViolations).isNotEmpty();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {-1, -99, -999})
        @DisplayName("Should fail when player score is not valid")
        void shouldFailWhenPlayerScoreIsNotValid (Integer invalidScores) {

            // Arrange
            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();
            testPlayerRequest.setScores(invalidScores);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            // Act
            Set<ConstraintViolation<PlayerRequestDto>> requestViolations =
                    validator.validate(testPlayerRequest);

            // Assert
            assertThat(requestViolations).isNotEmpty();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {
                "2026-08-03T10:00:00",
                "2026-08-02T15:00:00",
                "2026-12-25T12:00:00",
                "2026-01-01T00:00:00"
        })
        @DisplayName("Should fail when player timestamp is not valid")
        void shouldFailWhenPlayerTimestampIsNotValid (String dateTimeStrings) {

            // Arrange
            LocalDateTime invalidDateTime = dateTimeStrings != null ?
                    LocalDateTime.parse(dateTimeStrings) : null;

            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();
            testPlayerRequest.setTimestamp(invalidDateTime);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            // Act
            Set<ConstraintViolation<PlayerRequestDto>> requestViolations =
                    validator.validate(testPlayerRequest);

            // Assert
            assertThat(requestViolations).isNotEmpty();
        }
    }
}
