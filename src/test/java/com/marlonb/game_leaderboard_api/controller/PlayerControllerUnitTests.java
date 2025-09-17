package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.security.JwtFilter;
import com.marlonb.game_leaderboard_api.service.GameUserDetailsService;
import com.marlonb.game_leaderboard_api.service.JWTService;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import com.marlonb.game_leaderboard_api.test_securityConfig.TestSecurityConfig;
import lombok.With;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PlayerController.class)
@Import(TestSecurityConfig.class)
public class PlayerControllerUnitTests {

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

    @Nested
    class PositiveTests {

        @Test
        @WithMockUser(username = "1", roles = "USER")
        @DisplayName("Player(CREATE): Should create user successfully")
        void shouldCreateUserSuccessfully () throws Exception {

            PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
            PlayerRequestDto testPlayerRequest = PlayerTestData.samplePlayerRequest();
            PlayerResponseDto testPlayerResponse = PlayerTestData.samplePlayerResponse(testPlayer);

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
    }
}
