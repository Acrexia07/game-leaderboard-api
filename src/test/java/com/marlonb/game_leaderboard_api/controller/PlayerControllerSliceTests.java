package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.*;
import com.marlonb.game_leaderboard_api.model.user.UserPrincipal;
import com.marlonb.game_leaderboard_api.service.GameUserDetailsService;
import com.marlonb.game_leaderboard_api.service.JWTService;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import com.marlonb.game_leaderboard_api.test_data.Player2TestData;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import com.marlonb.game_leaderboard_api.test_securityConfig.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PlayerController.class)
@Import(TestSecurityConfig.class)
public class PlayerControllerSliceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private Authentication authentication;

    @MockitoBean
    private PlayerService playerService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private GameUserDetailsService gameUserDetailsService;

    private PlayerResponseDto testPlayerResponse;
    private Long testPlayerId;
    private UserPrincipal testPrincipal;
    private Long testPrincipalPlayerId;
    private PlayerUpdateDto testPlayerUpdate;

    @BeforeEach
    void setup () {
        PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
        testPlayerId = testPlayer.getId();

        testPlayerResponse = PlayerTestData.samplePlayerResponse(testPlayer);
        testPlayerUpdate = PlayerTestData.samplePlayerUpdate();

        testPrincipal = PlayerTestData.sampleUserPrincipal();
        testPrincipalPlayerId = testPrincipal.getPlayerId();
    }


    @Nested
    class PositiveTests {

        @Test
        @WithMockUser(username = "1", roles = "USER")
        @DisplayName("Player(CREATE): Should create player successfully")
        void shouldCreatePlayerSuccessfully () throws Exception {

            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();

            when(playerService.savePlayerData(eq(testPlayerRequest), any(Authentication.class)))
                    .thenReturn(testPlayerResponse);

            String jsonPlayerRequest = mapper.writeValueAsString(testPlayerRequest);

            mockMvc.perform(post("/api/players")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPlayerRequest))
                   .andExpectAll(
                           status().isCreated(),
                           header().exists("Location"),
                           header().string("Location", containsString("/api/players/")),
                           jsonPath("$.apiMessage").value("Player created successfully!"),
                           jsonPath("$.response.playerName").value(testPlayerRequest.getPlayerName()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Player(READ): Should retrieve all players successfully")
        void shouldRetrieveAllPlayersSuccessfully () throws Exception {

            PlayerEntity testPlayer2 = Player2TestData.samplePlayerData2();
            PlayerResponseDto testPlayer2Response = Player2TestData.samplePlayerResponse2(testPlayer2);

            List<PlayerResponseDto> expectedResponses = List.of(testPlayerResponse, testPlayer2Response);

            when(playerService.retrieveAllPlayersData())
                    .thenReturn(expectedResponses);

            String jsonListOfPlayersResponse = mapper.writeValueAsString(expectedResponses);

            mockMvc.perform(get("/api/players")
                            .content(jsonListOfPlayersResponse)
                            .contentType(MediaType.APPLICATION_JSON))
                   .andExpectAll(
                           status().isOk(),
                           jsonPath("$.apiMessage").value("Retrieved all players successfully!"),
                           jsonPath("$.response.length()").value(2));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Player(READ): Should retrieve specific player data successfully by Admin")
        void shouldRetrieveSpecificPlayerDataSuccessfullyByAdmin () throws Exception {

            when(playerService.retrieveSpecificPlayerData(testPlayerId))
                    .thenReturn(testPlayerResponse);

            String jsonTestPlayerResponse = mapper.writeValueAsString(testPlayerResponse);

            mockMvc.perform(get("/api/players/{id}", testPlayerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonTestPlayerResponse))
                   .andExpectAll(
                           status().isOk(),
                           jsonPath("$.apiMessage")
                                   .value("Retrieved specific player successfully!"),
                           jsonPath("$.response.playerName")
                                   .value(testPlayerResponse.playerName()));
        }

        @Test
        @DisplayName("Player(READ): Should retrieve player profile successfully!")
        void shouldRetrievePlayerProfileSuccessfully () throws Exception {

            PlayerSummaryDto testPlayerSummary = PlayerTestData.samplePlayerSummary();

            when(playerService.getPlayerProfile(testPrincipalPlayerId))
                    .thenReturn(testPlayerSummary);

            String jsonPlayerProfile = mapper.writeValueAsString(testPlayerSummary);

            mockMvc.perform(get("/api/players/me")
                            .with(user(testPrincipal))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPlayerProfile))
                   .andExpectAll(
                           status().isOk(),
                           jsonPath("$.apiMessage").value("Retrieve player profile successfully!"),
                           jsonPath("$.response.playerName").value(testPlayerSummary.playerName()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Player(UPDATE): Should update specific player data successfully by Admin")
        void shouldUpdateSpecificPlayerDataSuccessfullyByAdmin () throws Exception {

            PlayerResponseDto testPlayerResponseAfterUpdate = PlayerTestData.samplePlayerResponseAfterUpdate();

            when(playerService.updateSpecificPlayerData(testPlayerId, testPlayerUpdate))
                    .thenReturn(testPlayerResponseAfterUpdate);

            String jsonPlayerUpdate = mapper.writeValueAsString(testPlayerUpdate);

            mockMvc.perform(put("/api/players/{id}", testPlayerId)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPlayerUpdate))
                   .andExpectAll(
                           status().isOk(),
                           jsonPath("$.apiMessage")
                                   .value("Specific player updated successfully!"),
                           jsonPath("$.response.playerName").value(testPlayerUpdate.getPlayerName()));
        }

        @Test
        @DisplayName("Player(READ): Should update player profile successfully!")
        void shouldUpdatePlayerProfileSuccessfully () throws Exception {

            PlayerSummaryDto testProfileUpdate = PlayerTestData.samplePlayerUpdateSummary();

            when(playerService.updatePlayerProfile(testPrincipalPlayerId, testPlayerUpdate))
                    .thenReturn(testProfileUpdate);

            String jsonProfileUpdate = mapper.writeValueAsString(testPlayerUpdate);

            mockMvc.perform(put("/api/players/me")
                            .with(user(testPrincipal))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonProfileUpdate))
                   .andExpectAll(
                           status().isOk(),
                           jsonPath("$.apiMessage")
                                   .value("Update player profile successfully!"));
        }
    }

    @Nested
    class NegativeTests {

        @Test
        @WithMockUser(username = "1", roles = "USER")
        @DisplayName("Player(CREATE): Should fail to create when player data is invalid")
        void shouldFailToCreateWhenPlayerDataIsInvalid () throws Exception {

            PlayerRequestDto invalidTestPlayerRequest = PlayerTestData.sampleInvalidPlayerRequest();

            String jsonInvalidPlayerRequest = mapper.writeValueAsString(invalidTestPlayerRequest);

            mockMvc.perform(post("/api/players")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvalidPlayerRequest))
                    .andExpect(status().isBadRequest());

        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Player(READ): Should fail to retrieve when player id does not exists")
        void shouldFailToRetrieveWhenPlayerIdDoesNotExists () throws Exception {

            final long nonExistentId = 100L;

            when(playerService.retrieveSpecificPlayerData(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Resource not found!"));

            mockMvc.perform(get("/api/players/{id}", nonExistentId))
                   .andExpectAll(
                           status().isNotFound(),
                           jsonPath("$.message").value("Resource not found!"));
        }

        @Test
        @DisplayName("Player(READ): Should fail to retrieve player profile if player id does not exist")
        void shouldFailToRetrievePlayerProfileIfPlayerIdDoesNotExist () throws Exception {

            UserPrincipal principal = PlayerTestData.sampleUserWithoutPlayerAccount();

            mockMvc.perform(get("/api/players/me")
                            .with(user(principal)))
                   .andExpectAll(
                           status().isNotFound(),
                           jsonPath("$.message")
                                   .value("Resource not found!"),
                           jsonPath("$.error.resource")
                                   .value("Player account not created yet for this user"));
        }
    }
}
