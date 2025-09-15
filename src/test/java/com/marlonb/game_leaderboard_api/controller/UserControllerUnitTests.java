package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.game_leaderboard_api.model.user.LoginRequestDto;

import com.marlonb.game_leaderboard_api.service.GameUserDetailsService;
import com.marlonb.game_leaderboard_api.service.JWTService;
import com.marlonb.game_leaderboard_api.service.UserService;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import com.marlonb.game_leaderboard_api.test_securityConfig.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.marlonb.game_leaderboard_api.exception.ErrorMessages.BAD_CREDENTIALS_MESSAGE;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private GameUserDetailsService gameUserDetailsService;

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Login: Should pass login when user has valid credentials")
        void shouldPassLoginWhenUserHasValidCredentials() throws Exception {

            // Arrange
            LoginRequestDto testUserLogin = User1TestData.sampleUser1LoginData();
            String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTczNTYwMzIwMCwiZXhwIjoxNzM1Njg5NjAwfQ.signature";

            when(userService.verifyUser(testUserLogin)).thenReturn(expectedToken);

            String jsonLoginRequest = mapper.writeValueAsString(testUserLogin);

            // Act & Assert
            mockMvc.perform(post("/api/users/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonLoginRequest))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage").value("User login successfully!"),
                            jsonPath("$.response").value(expectedToken));
        }


    }


    @Nested
    class NegativeTests {

        @Test
        @DisplayName("Login: Should fail login when user has invalid credentials")
        void shouldFailLoginWhenUserHasInvalidCredentials () throws Exception {

            LoginRequestDto testUserInvalidLogin = User1TestData.sampleUser1InvalidLoginData();

            when(userService.verifyUser(testUserInvalidLogin))
                    .thenThrow(new BadCredentialsException(BAD_CREDENTIALS_MESSAGE.getErrorMessage()));

            String jsonInvalidLoginRequest = mapper.writeValueAsString(testUserInvalidLogin);

            // Act & Assert
            mockMvc.perform(post("/api/users/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvalidLoginRequest))
                    .andExpectAll(
                            status().isUnauthorized());
        }

    }

}
