package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;
import com.marlonb.game_leaderboard_api.model.user.UserUpdateDto;
import com.marlonb.game_leaderboard_api.security.BasicAuthenticationConfig;
import com.marlonb.game_leaderboard_api.service.UserService;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import com.marlonb.game_leaderboard_api.test_data.user.User2TestData;
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

import java.util.List;

import static com.marlonb.game_leaderboard_api.exception.ErrorMessages.*;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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

        @Test
        @DisplayName("Should pass when retrieve all user data")
        void shouldPassWhenRetrieveAllUserDataSuccessfully () throws Exception {

            UserEntity testUser1 = User1TestData.sampleUser1Data();
            UserEntity testUser2 = User2TestData.sampleUser2Data();

            UserResponseDto testUser1Response = User1TestData.sampleUser1Response();
            UserResponseDto testUser2Response = User2TestData.sampleUser2Response();

            List<UserResponseDto> expectedResponse = List.of(testUser1Response, testUser2Response);

            when(userService.retrieveAllUsers()).thenReturn(expectedResponse);

            String jsonListOfUserResponse = mapper.writeValueAsString(expectedResponse);

            mockMvc.perform(get("/api/users")
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonListOfUserResponse))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage").value("Retrieved all users successfully!"),
                            jsonPath("$.response").isArray(),
                            jsonPath("$.response[*].username",
                                    hasItems(testUser1Response.username(), testUser2Response.username())),
                            jsonPath("$.response.length()").value(2));
        }

        @Test
        @DisplayName("Should pass when retrieve specific user data")
        void shouldPassWhenRetrieveSpecificUserDataSuccessfully () throws Exception {

            UserEntity testUser = User1TestData.sampleUser1Data();
            final long testUserId = testUser.getId();

            UserResponseDto testUserResponse = User1TestData.sampleUser1Response();

            when(userService.retrieveSpecificUser(testUserId))
                    .thenReturn(testUserResponse);

            String jsonUserResponse = mapper.writeValueAsString(testUserResponse);

            mockMvc.perform(get("/api/users/{id}", testUserId)
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonUserResponse))
                   .andExpectAll(
                           status().isOk(),
                           jsonPath("$.apiMessage").value("Retrieved specific user successfully!"),
                           jsonPath("$.response.username").value(testUserResponse.username()));
        }

        @Test
        @DisplayName("Should pass when update specific user")
        void shouldPassWhenUpdateSpecificUser () throws Exception {

            UserEntity testUser = User1TestData.sampleUser1Data();
            final long testUserId = testUser.getId();

            UserUpdateDto testUserUpdate = User1TestData.sampleUser1Update();
            UserResponseDto testUserResponse = User1TestData.sampleUser1Response();

            when(userService.updateSpecificUser(testUserId, testUserUpdate))
                    .thenReturn(testUserResponse);

            String jsonUserUpdate = mapper.writeValueAsString(testUserUpdate);

            mockMvc.perform(put("/api/users/{id}", testUserId)
                            .with(httpBasic("acrexia", "dummy"))
                            .content(jsonUserUpdate)
                            .contentType("application/json"))
                   .andExpectAll(
                           status().isOk(),
                           jsonPath("$.apiMessage").value("Updated specific user successfully!"),
                           jsonPath("$.response.username").value(testUserResponse.username()));
        }

        @Test
        @DisplayName("Should pass when delete specific user")
        void shouldPassWhenDeleteSpecificUser () throws Exception {

            UserEntity testUser = User1TestData.sampleUser1Data();
            final long testUserId = testUser.getId();

            doNothing().when(userService).deleteSpecificUser(testUserId);

            mockMvc.perform(delete("/api/users/{id}", testUserId)
                            .with(httpBasic("acrexia", "dummy")))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    class NegativeTesting {

        @Test
        @DisplayName("Should return an error status on create request when username already exist")
        void shouldReturnAnErrorStatusOnCreateRequestWhenUsernameAlreadyExist () throws Exception {

            UserResponseDto testUserResponse = User1TestData.sampleUser1Response();

            when(userService.createUser(any()))
                    .thenThrow(new DuplicateResourceFoundException
                               (DUPLICATE_RESOURCE_FOUND_MESSAGE.getErrorMessage()));

            String jsonUserResponse = mapper.writeValueAsString(testUserResponse);

            mockMvc.perform(post("/api/users/register")
                            .contentType("application/json")
                            .content(jsonUserResponse))
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.message")
                                    .value(DUPLICATE_RESOURCE_FOUND_MESSAGE.getErrorMessage()));
        }

        @Test
        @DisplayName("Should return an error status on update request when username already exist")
        void shouldReturnAnErrorStatusOnUpdateRequestWhenUsernameAlreadyExist () throws Exception {

            UserEntity testUser = User1TestData.sampleUser1Data();
            final long testUserId = testUser.getId();

            UserResponseDto testUserResponse = User1TestData.sampleUser1Response();

            when(userService.updateSpecificUser(eq(testUserId), any(UserUpdateDto.class)))
                    .thenThrow(new DuplicateResourceFoundException
                            (DUPLICATE_RESOURCE_FOUND_MESSAGE.getErrorMessage()));

            String jsonUserResponse = mapper.writeValueAsString(testUserResponse);

            mockMvc.perform(put("/api/users/{id}", testUserId)
                            .with(httpBasic("acrexia", "dummy"))
                            .contentType("application/json")
                            .content(jsonUserResponse))
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.message")
                                    .value(DUPLICATE_RESOURCE_FOUND_MESSAGE.getErrorMessage()));
        }

        @Test
        @DisplayName("Should return an error status on read request when user id does not exist")
        void shouldReturnAnErrorStatusOnReadRequestWhenUserIdDoesNotExist () throws Exception {

            final long nonExistentId = 100L;

            when(userService.retrieveSpecificUser(eq(nonExistentId)))
                    .thenThrow(new ResourceNotFoundException
                               (RESOURCE_NOT_FOUND_MESSAGE.getErrorMessage()));

            mockMvc.perform(get("/api/users/{id}", nonExistentId)
                            .with(httpBasic("acrexia", "dummy")))
                    .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message")
                                .value(RESOURCE_NOT_FOUND_MESSAGE.getErrorMessage()));
        }

        @Test
        @DisplayName("Should return error status on create request when there is a null or missing input")
        void shouldReturnStatusOnCreateRequestWhenThereIsANullOrMissingInput () throws Exception {

            UserRequestDto testUserRequest = User1TestData.sampleUser1Request();
            testUserRequest.setPassword(null);

            String jsonUserRequest = mapper.writeValueAsString(testUserRequest);

            mockMvc.perform(post("/api/users/register")
                            .contentType("application/json")
                            .content(jsonUserRequest))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message")
                                    .value(VALIDATION_ERROR_MESSAGE.getErrorMessage()));
        }

    }
}
