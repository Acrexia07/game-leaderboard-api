package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.PlayerUpdateDto;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import com.marlonb.game_leaderboard_api.test_data.Player2TestData;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.marlonb.game_leaderboard_api.exception.ErrorHeaders.*;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
public class PlayerControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    private ObjectMapper mapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public PlayerService playerService () {
            return mock(PlayerService.class);
        }
    }

    @BeforeEach
    void initSetup () {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        reset(playerService);
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should pass when create new player")
        void shouldPassWhenCreateNewPlayer () throws Exception {

            PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
            PlayerResponseDto playerResponseDto = PlayerTestData.samplePlayerResponse(testPlayer);
            PlayerRequestDto playerRequestDto = PlayerTestData.samplePlayerRequest();

            when(playerService.savePlayerData(any())).thenReturn(playerResponseDto);

            String jsonPlayerRequest = mapper.writeValueAsString(playerRequestDto);

            mockMvc.perform(post("/api/players")
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonPlayerRequest))
                   .andExpectAll(
                           status().isCreated(),
                           header().exists("location"),
                           header().string("location", "/api/players/" + playerResponseDto.id()),
                           jsonPath("$.apiMessage").value("Player created successfully!"));
        }

        @Test
        @DisplayName("Should pass when retrieve all player resource")
        void shouldPassWhenRetrieveAllPlayerResource () throws Exception {

            PlayerEntity testPlayer1 = PlayerTestData.samplePlayerData();
            PlayerResponseDto testPlayerResponse1 = PlayerTestData.samplePlayerResponse(testPlayer1);

            PlayerEntity testPlayer2 = Player2TestData.samplePlayerData2();
            PlayerResponseDto testPlayerResponse2 = Player2TestData.samplePlayerResponse2(testPlayer2);

            List<PlayerResponseDto> expectedResponses = List.of(testPlayerResponse1, testPlayerResponse2);

            when(playerService.retrieveAllPlayersData()).thenReturn(expectedResponses);

            String jsonListOfPlayerResponses = mapper.writeValueAsString(expectedResponses);

            mockMvc.perform(get("/api/players")
                        .with(csrf())
                        .with(httpBasic("acrexia", "dummy"))
                        .contentType("application/json")
                        .content(jsonListOfPlayerResponses))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage").value("Retrieved all players successfully!"),
                            jsonPath("$.response").isArray(),
                            jsonPath("$.response[*].playerName",
                                     hasItems(testPlayerResponse1.playerName(), testPlayerResponse2.playerName())),
                            jsonPath("$.response.length()").value(2));
        }

        @Test
        @DisplayName("Should pass when retrieve specific player resource")
        void shouldPassWhenRetrieveSpecificPlayerResource () throws  Exception {

            PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
            final long testPlayerId = testPlayer.getId();

            PlayerResponseDto playerResponseDto = PlayerTestData.samplePlayerResponse(testPlayer);

            when(playerService.retrieveSpecificPlayerData(testPlayerId)).thenReturn(playerResponseDto);

            String jsonPlayerResponse = mapper.writeValueAsString(playerResponseDto);

            mockMvc.perform(get("/api/players/{id}", testPlayerId)
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonPlayerResponse))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage")
                                    .value("Specific player data retrieved successfully!"),
                            jsonPath("$.response.id").exists(),
                            jsonPath("$.response.playerName").value(testPlayer.getPlayerName()));
        }

        @Test
        @DisplayName("Should pass when update specific player resource")
        void shouldPassWhenUpdateSpecificPlayerResource () throws Exception {

            PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
            final long testPlayerId = testPlayer.getId();

            PlayerUpdateDto playerUpdateDto = PlayerTestData.samplePlayerUpdate();
            PlayerResponseDto playerResponseDto = PlayerTestData.samplePlayerResponse(testPlayer);

            when(playerService.updateSpecificPlayerData(testPlayerId, playerUpdateDto))
                    .thenReturn(playerResponseDto);

            String jsonPlayerResponse = mapper.writeValueAsString(playerUpdateDto);

            mockMvc.perform(put("/api/players/{id}", testPlayerId)
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonPlayerResponse))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage")
                                    .value("Specific player updated successfully!"),
                            jsonPath("$.response.id").exists(),
                            jsonPath("$.response.playerName").value(playerUpdateDto.getPlayerName()));
        }

        @Test
        @DisplayName("Should pass when delete specific player resource")
        void shouldPassWhenDeleteSpecificPlayerResource () throws Exception {

            PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
            final long testPlayerId = testPlayer.getId();

            doNothing().when(playerService).deleteSpecificPlayerData(testPlayerId);

            mockMvc.perform(delete("/api/players/{id}", testPlayerId)
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy")))
                   .andExpectAll(status().isNoContent());
        }
    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("Should return error status when Player name already exists")
        void shouldReturnErrorStatusWhenPlayerNameAlreadyExist () throws Exception {

            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();

            when(playerService.savePlayerData(any()))
                    .thenThrow(new DuplicateResourceFoundException
                                   (DUPLICATE_RESOURCE_FOUND_MESSAGE.getErrorMessage()));

            String jsonPlayerRequest = mapper.writeValueAsString(testPlayerRequest);

            mockMvc.perform(post("/api/players")
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonPlayerRequest))
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.generalErrorMessage")
                                    .value(DUPLICATE_RESOURCE_FOUND_MESSAGE.getErrorMessage()));
        }

        @Test
        @DisplayName("Should return error status on read when player id does not exist")
        void shouldReturnErrorStatusOnReadWhenPlayerIdDoesNotExist () throws Exception {

            final long nonExistentId = 50L;

            when(playerService.retrieveSpecificPlayerData(nonExistentId))
                    .thenThrow(new ResourceNotFoundException
                                   (RESOURCE_NOT_FOUND_MESSAGE.getErrorMessage()));

            mockMvc.perform(get("/api/players/{id}", nonExistentId)
                        .with(httpBasic("acrexia", "dummy")))
                   .andExpectAll(
                           status().isNotFound(),
                           jsonPath("$.generalErrorMessage")
                                   .value(RESOURCE_NOT_FOUND_MESSAGE.getErrorMessage()));
        }

        @Test
        @DisplayName("Should return error status when there is a null or missing input")
        void shouldReturnErrorStatusWhenThereIsANullOrMissingInput () throws Exception {

            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();
            testPlayerRequest.setPlayerName(null);

            String jsonPlayerRequest = mapper.writeValueAsString(testPlayerRequest);

            mockMvc.perform(post("/api/players")
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonPlayerRequest))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.generalErrorMessage")
                                    .value(VALIDATION_ERROR_MESSAGE.getErrorMessage()));
        }

    }
}
