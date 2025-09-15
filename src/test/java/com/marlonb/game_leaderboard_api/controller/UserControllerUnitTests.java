package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.game_leaderboard_api.model.user.AdminUserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.LoginRequestDto;

import com.marlonb.game_leaderboard_api.model.user.UserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;
import com.marlonb.game_leaderboard_api.service.GameUserDetailsService;
import com.marlonb.game_leaderboard_api.service.JWTService;
import com.marlonb.game_leaderboard_api.service.UserService;
import com.marlonb.game_leaderboard_api.test_data.user.AdminUser1TestData;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.marlonb.game_leaderboard_api.exception.ErrorMessages.BAD_CREDENTIALS_MESSAGE;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
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
        @DisplayName("Register(Public User): Should register successfully when public user has valid credentials")
        void shouldRegisterSuccessfullyWhenPublicUserHasValidCredentials () throws Exception {

            UserRequestDto testPublicUserRequest = User1TestData.sampleUser1Request();
            UserResponseDto testPublicUserResponse = User1TestData.sampleUser1Response();

            when(userService.createUser(any(UserRequestDto.class)))
                    .thenReturn(testPublicUserResponse);

            String jsonPublicUserRequest = mapper.writeValueAsString(testPublicUserRequest);

            mockMvc.perform(post("/api/users/register")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPublicUserRequest))
                   .andExpectAll(
                           status().isCreated(),
                           header().exists("Location"),
                           header().string("Location", containsString("/api/users/register/")),
                           jsonPath("$.apiMessage").value("User created successfully!"),
                           jsonPath("$.response.username").value(testPublicUserRequest.getUsername()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Register(Admin User): Should register successfully when admin user has valid credentials")
        void shouldRegisterSuccessfullyWhenAdminUserHasValidCredentials () throws Exception {

            AdminUserRequestDto testAdminUserRequest = AdminUser1TestData.sampleAdminUser1Request();
            UserResponseDto testAdminUserResponse = AdminUser1TestData.sampleAdminUser1Response();

            when(userService.createAdminUser(testAdminUserRequest))
                    .thenReturn(testAdminUserResponse);

            String jsonAdminUserRequest = mapper.writeValueAsString(testAdminUserRequest);

            mockMvc.perform(post("/api/users")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonAdminUserRequest))
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            header().string("Location", containsString("/api/users/")),
                            jsonPath("$.apiMessage").value("Admin created successfully!"),
                            jsonPath("$.response.username").value(testAdminUserRequest.getUsername()));
        }

        @Test
        @DisplayName("Login: Should login when user has valid credentials")
        void shouldPassLoginWhenUserHasValidCredentials() throws Exception {

            LoginRequestDto testUserLogin = User1TestData.sampleUser1LoginData();
            String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTczNTYwMzIwMCwiZXhwIjoxNzM1Njg5NjAwfQ.signature";

            when(userService.verifyUser(testUserLogin)).thenReturn(expectedToken);

            String jsonLoginRequest = mapper.writeValueAsString(testUserLogin);

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
        @DisplayName("Login: Should fail to login when user has invalid credentials")
        void shouldFailToLoginWhenUserHasInvalidCredentials () throws Exception {

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

        @Test
        @DisplayName("Register(Public User): Should fail to register when user is invalid")
        void shouldFailToRegisterWhenUserIsInvalid () throws Exception {

            UserRequestDto invalidTestUserRequest = User1TestData.sampleInvalidUser1Request();

            String jsonInvalidLoginRequest = mapper.writeValueAsString(invalidTestUserRequest);

            mockMvc.perform(post("/api/users/register")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvalidLoginRequest))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Register(Admin User): Should fail to register when admin user is invalid")
        void shouldFailToRegisterWhenAdminUserIsInvalid () throws Exception {

            AdminUserRequestDto invalidTestAdminRequest = AdminUser1TestData.sampleAdminUser1InvalidRequest();

            String jsonInvalidAdminRequest = mapper.writeValueAsString(invalidTestAdminRequest);

            mockMvc.perform(post("/api/users")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvalidAdminRequest))
                    .andExpect(status().isBadRequest());
        }
    }

}
