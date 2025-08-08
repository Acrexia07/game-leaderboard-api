package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerRequestDto;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.service.PlayerService;
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

    @TestConfiguration
    static class TestConfig {

        @Bean
        public PlayerService playerService () {
            return mock(PlayerService.class);
        }
    }

    @BeforeEach
    void initSetup () {
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

            String jsonPlayerRequest = new ObjectMapper().writeValueAsString(playerRequestDto);

            mockMvc.perform(post("/api/players")
                            .with(csrf())
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonPlayerRequest))
                   .andExpectAll(
                           status().isCreated(),
                           header().exists("Location"),
                           header().string("Location", "/api/players"),
                           jsonPath("$.message").value("Player created successfully!"),
                           jsonPath("$.data.playerName").value(playerRequestDto.getPlayerName()));
        }
    }

//    @Nested
//    class NegativeTests {
//
//    }
}
