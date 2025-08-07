package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.*;
import com.marlonb.game_leaderboard_api.repository.PlayerRepository;
import com.marlonb.game_leaderboard_api.test_data.Player2TestData;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
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
import java.util.Optional;
import java.util.Set;

import static com.marlonb.game_leaderboard_api.test_assertions.PlayerTestAssertions.playerAssertRecursionComparisons;
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

    private PlayerEntity testPlayer;
    long testPlayerId;
    private PlayerResponseDto expectedResponse;
    private PlayerRequestDto testPlayerRequest;

    @BeforeEach
    void initSetup () {
        testPlayer = PlayerTestData.samplePlayerData();
        testPlayerId = testPlayer.getId();
        expectedResponse = PlayerTestData.samplePlayerResponse(testPlayer);
        testPlayerRequest = PlayerTestData.samplePlayerRequest();
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should create player successfully")
        void shouldCreatePlayerSuccessfully () {

            when(playerMapper.toEntity(any(PlayerRequestDto.class)))
                    .thenReturn(testPlayer);

            when(playerRepository.save(any(PlayerEntity.class)))
                    .thenReturn(testPlayer);

            when(playerMapper.toResponse(any(PlayerEntity.class)))
                    .thenReturn(expectedResponse);

            PlayerResponseDto actualResponse = playerService.savePlayerData(testPlayerRequest);

            playerAssertRecursionComparisons(testPlayer, actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("Should retrieve all players data successfully")
        void shouldRetrieveAllPlayersDataSuccessfully () {

            PlayerEntity testPlayer1 = PlayerTestData.samplePlayerData();
            PlayerEntity testPlayer2 = Player2TestData.samplePlayerData2();

            PlayerResponseDto playerResponse1 = PlayerTestData.samplePlayerResponse(testPlayer1);
            PlayerResponseDto playerResponse2 = Player2TestData.samplePlayerResponse2(testPlayer2);

            List<PlayerEntity> listOfPlayerEntities = List.of(testPlayer1, testPlayer2);
            List<PlayerResponseDto> expectedResponse = List.of(playerResponse1, playerResponse2);

            when(playerRepository.findAll()).thenReturn(listOfPlayerEntities);

            when(playerMapper.toResponse(testPlayer1)).thenReturn(playerResponse1);
            when(playerMapper.toResponse(testPlayer2)).thenReturn(playerResponse2);

            List<PlayerResponseDto> actualResponse = playerService.retrieveAllPlayersData();

            playerAssertRecursionComparisons(testPlayer1, testPlayer2, actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("Should retrieve specific player data successfully")
        void shouldRetrieveSpecificPlayerDataSuccessfully () {

            when(playerRepository.findById(testPlayerId))
                    .thenReturn(Optional.of(testPlayer));

            when(playerMapper.toResponse(testPlayer))
                    .thenReturn(expectedResponse);

            PlayerResponseDto actualResponse = playerService.retrieveSpecificPlayerData(testPlayerId);

            playerAssertRecursionComparisons(testPlayer, actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("Should update specific player data successfully")
        void shouldUpdateSpecificPlayerDataSuccessfully () {

            PlayerUpdateDto playerUpdateDto = PlayerTestData.samplePlayerUpdate();
            PlayerEntity testPlayerAfterUpdate = PlayerTestData.samplePlayerDataAfterUpdate();

            when(playerRepository.findById(testPlayerId))
                    .thenReturn(Optional.of(testPlayer));

            doNothing().when(playerMapper)
                    .toUpdateFromEntity(testPlayer, playerUpdateDto);

            when(playerRepository.save(any(PlayerEntity.class)))
                    .thenReturn(testPlayerAfterUpdate);

            when(playerMapper.toResponse(testPlayerAfterUpdate))
                    .thenReturn(expectedResponse);

            PlayerResponseDto actualResponse = playerService.updateSpecificPlayerData(testPlayerId, playerUpdateDto);

            playerAssertRecursionComparisons(testPlayer, actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("Should delete specific player data successfully")
        void shouldDeleteSpecificPlayerDataSuccessfully () {

            when(playerRepository.findById(testPlayerId))
                    .thenReturn(Optional.of(testPlayer));

            playerService.deleteSpecificPlayerData(testPlayerId);

            verify(playerRepository).deleteById(testPlayerId);
        }
    }

    @Nested
    class NegativeTests {

        @Nested
        class AttributeTests {
            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"", "Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do."})
            @DisplayName("Should fail when player name is not valid")
            void shouldFailWhenPlayerNameIsNotValid (String invalidPlayerNames) {

                testPlayerRequest.setPlayerName(invalidPlayerNames);

                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();

                Set<ConstraintViolation<PlayerRequestDto>> requestViolations =
                        validator.validate(testPlayerRequest);

                assertThat(requestViolations).isNotEmpty();
            }

            @ParameterizedTest
            @NullSource
            @ValueSource(ints = {-1, -99, -999})
            @DisplayName("Should fail when player score is not valid")
            void shouldFailWhenPlayerScoreIsNotValid (Integer invalidScores) {

                testPlayerRequest.setScores(invalidScores);

                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();

                Set<ConstraintViolation<PlayerRequestDto>> requestViolations =
                        validator.validate(testPlayerRequest);

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

                LocalDateTime invalidDateTime = dateTimeStrings != null ?
                        LocalDateTime.parse(dateTimeStrings) : null;

                testPlayerRequest.setTimestamp(invalidDateTime);

                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();

                Set<ConstraintViolation<PlayerRequestDto>> requestViolations =
                        validator.validate(testPlayerRequest);

                assertThat(requestViolations).isNotEmpty();
            }
        }

        @Nested
        class CrudOperations {

            @Test
            @DisplayName("Should fail when player Id does not exist")
            void shouldFailWhenPlayerIdDoesNotExist () {

                final long nonExistentId = 100L;

                when(playerRepository.findById(nonExistentId))
                        .thenReturn(Optional.empty());

                Assertions.assertThrows(ResourceNotFoundException.class,
                        () -> playerService.findPlayerId(nonExistentId));

                verify(playerRepository).findById(nonExistentId);
            }

            @Test
            @DisplayName("Should fail create when player name already exist")
            void shouldFailCreateWhenPlayerNameAlreadyExist () {

                when(playerMapper.toEntity(any(PlayerRequestDto.class)))
                        .thenReturn(testPlayer);

                when(playerRepository.existsByPlayerName("player1")).thenReturn(true);

                Assertions.assertThrows(DuplicateResourceFoundException.class,
                                        () -> playerService.savePlayerData(testPlayerRequest));

                verify(playerRepository, never()).save(any());
            }

            @Test
            @DisplayName("Should fail update when player name already exist")
            void shouldFailUpdateWhenPlayerNameAlreadyExist () {

                PlayerEntity testPlayer2 = Player2TestData.samplePlayerData2();
                final long testPlayer2Id = testPlayer2.getId();

                PlayerUpdateDto testPlayer2Update = Player2TestData.samplePlayerUpdate2();
                testPlayer2Update.setPlayerName("player1");

                when(playerRepository.findById(testPlayer2Id))
                        .thenReturn(Optional.of(testPlayer2));

                when(playerRepository.existsByPlayerName("player1"))
                        .thenReturn(true);

                Assertions.assertThrows(DuplicateResourceFoundException.class,
                        () -> playerService.updateSpecificPlayerData(testPlayer2Id, testPlayer2Update));

                verify(playerRepository, never()).save(any());
            }
        }
    }
}
