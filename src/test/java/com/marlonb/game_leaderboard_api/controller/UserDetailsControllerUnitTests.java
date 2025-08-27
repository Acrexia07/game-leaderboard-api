package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;
import com.marlonb.game_leaderboard_api.security.BasicAuthenticationConfig;
import com.marlonb.game_leaderboard_api.service.UserService;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(BasicAuthenticationConfig.class)
public class UserDetailsControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private ObjectMapper mapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public UserService userService () {
            return mock(UserService.class);
        }
    }

    @BeforeEach
    void initSetup () {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        reset(userService);
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should pass when create new user")
        void shouldPassWhenCreateNewUser () throws Exception {

            UserResponseDto testUserResponse = User1TestData.sampleUser1Response();
            UserRequestDto testUserRequest = User1TestData.sampleUser1Request();

            when(userService.createUser(any()))
                    .thenReturn(testUserResponse);

            String jsonUserRequest = mapper.writeValueAsString(testUserRequest);

            mockMvc.perform(post("/api/users/register")
                            .content(jsonUserRequest)
                            .contentType("application/json"))
                   .andExpectAll(
                           status().isCreated(),
                           header().exists("location"),
                           header().string("location", "/api/users/register/" + testUserResponse.id()),
                           jsonPath("$.apiMessage").value("User created successfully!"));
        }
    }
}
