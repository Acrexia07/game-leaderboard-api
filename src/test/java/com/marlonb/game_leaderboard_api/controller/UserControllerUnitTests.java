package com.marlonb.game_leaderboard_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.user.*;

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

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import static com.marlonb.game_leaderboard_api.exception.ErrorMessages.BAD_CREDENTIALS_MESSAGE;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        @DisplayName("Register(CREATE): Should register successfully when public user has valid credentials")
        void shouldRegisterSuccessfullyWhenPublicUserHasValidCredentials () throws Exception {

            UserRequestDto testPublicUserRequest = User1TestData.sampleUser1Request();
            UserResponseDto testPublicUserResponse = User1TestData.sampleUser1ResponseForCreate();

            when(userService.createUser(any(UserRequestDto.class)))
                    .thenReturn(testPublicUserResponse);

            String jsonPublicUserRequest = mapper.writeValueAsString(testPublicUserRequest);
            mockMvc.perform(post("/api/users/register")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPublicUserRequest))
                    .andDo(print())
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
        @DisplayName("Login(CREATE): Should login when user has valid credentials")
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

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("User Management(READ): Should retrieve all users successfully")
        void shouldRetrieveAllUsersSuccessfully () throws Exception {

            UserResponseDto testPublicUser1 = User1TestData.sampleUser1Response();
            UserResponseDto testAdminUser1 = AdminUser1TestData.sampleAdminUser1Response();

            List<UserResponseDto> listOfUsers = List.of(testPublicUser1, testAdminUser1);

            when(userService.retrieveAllUsers()).thenReturn(listOfUsers);

            String jsonListOfUsersResponse = mapper.writeValueAsString(listOfUsers);

            mockMvc.perform(get("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonListOfUsersResponse))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage").value("Retrieved all users successfully!"),
                            jsonPath("$.response.length()").value(2));
        }

        @Test
        @WithMockUser(username = "1" , roles = "USER")
        @DisplayName("User Management(READ): Should retrieve specific user successfully")
        void shouldRetrieveSpecificUserSuccessfully () throws Exception {

            UserPrincipal testUserPrincipal = User1TestData.sampleUser1PrincipalData();
            UserResponseDto testUserPrincipalResponse = User1TestData.sampleUser1PrincipalResponse();

            final long testUserId = testUserPrincipal.getId();

            when(userService.retrieveSpecificUser(testUserId))
                    .thenReturn(testUserPrincipalResponse);

            String jsonUserResponse = mapper.writeValueAsString(testUserPrincipalResponse);

            mockMvc.perform(get("/api/users/{id}", testUserId)
                            .with(user(testUserPrincipal))
                            .contentType(MediaType.APPLICATION_JSON)
                            .contentType(jsonUserResponse))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage")
                                    .value("Retrieved specific user successfully!"),
                            jsonPath("$.response.username")
                                    .value(testUserPrincipalResponse.username()));
        }

        @Test
        @WithMockUser(username = "1", roles = "USER")
        @DisplayName("User Management(UPDATE): Should update specific user successfully")
        void shouldUpdateSpecificUserSuccessfully () throws Exception {

            UserPrincipal testUserPrincipal = User1TestData.sampleUser1PrincipalData();
            final long testUserId = testUserPrincipal.getId();

            UserUpdateDto testUserPrincipalUpdate = User1TestData.sampleUser1PrincipalUpdate();
            UserResponseDto testUserPrincipalResponseAfterUpdate = User1TestData.sampleUser1PrincipalResponseAfterUpdate();

            when(userService.updateSpecificUser(testUserId, testUserPrincipalUpdate))
                    .thenReturn(testUserPrincipalResponseAfterUpdate);

            String jsonUserResponse = mapper.writeValueAsString(testUserPrincipalUpdate);

            mockMvc.perform(put("/api/users/{id}", testUserId)
                            .with(user(testUserPrincipal))
                            .content(jsonUserResponse)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.apiMessage")
                                    .value("Updated specific user successfully!"),
                            jsonPath("$.response.username")
                                    .value(testUserPrincipalUpdate.getUsername()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("User Management(DELETE): Should delete specific user successfully")
        void shouldDeleteSpecificUserSuccessfully () throws Exception {

            UserEntity testAdminUser = AdminUser1TestData.sampleAdminUser1Data();
            final long testUserId = testAdminUser.getId();

            doNothing().when(userService).deleteSpecificUser(testUserId);

            mockMvc.perform(delete("/api/users/{id}", testUserId)
                            .with(csrf()))
                    .andExpect(status().isNoContent());
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

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("User Management(GENERAL): Should return error status when user id does not exist")
        void shouldReturnErrorStatusWhenUserIdDoesNotExist () throws Exception {

            final long nonExistentId = 100L;

            String RESOURCE_NOT_FOUND_ERROR_MESSAGE =
                    String.format("This user id '%s' does not exist!", nonExistentId);

            when(userService.retrieveSpecificUser(nonExistentId))
                    .thenThrow(new ResourceNotFoundException
                                (String.format(RESOURCE_NOT_FOUND_ERROR_MESSAGE)));

            mockMvc.perform(get("/api/users/{id}", nonExistentId))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.message").value("Resource not found!"),
                            jsonPath("$.error.resource").value(RESOURCE_NOT_FOUND_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("User Management(READ): Should deny public user accessing other user's data")
        void shouldDenyPublicUserAccessingOtherUsersData () throws Exception {

            UserPrincipal testUserPrincipal = User1TestData.sampleUser1PrincipalData();
            final long otherUserId = 2L;

            when(userService.retrieveSpecificUser(otherUserId))
                    .thenThrow(new AccessDeniedException("Access Denied!"));

            mockMvc.perform(get("/api/users/{id}", otherUserId)
                            .with(user(testUserPrincipal)))
                    .andExpectAll(
                            status().isForbidden(),
                            jsonPath("$.message")
                                    .value("Forbidden access – insufficient permissions."),
                            jsonPath("$.error.credentials[0]").value("Access Denied"));

        }

        @Test
        @DisplayName("User Management(READ): Should fail to update if user data input is invalid")
        void shouldFailToUpdateIfUserDataInputIsInvalid () throws Exception {

            UserPrincipal testUserPrincipal = User1TestData.sampleUser1PrincipalData();
            final long testUserId = testUserPrincipal.getId();

            UserUpdateDto invalidTestUserPrincipal =
                    User1TestData.sampleUser1PrincipalInvalidUpdate();
            UserResponseDto invalidTestUserPrincipalResponse =
                    User1TestData.sampleUser1PrincipalResponseAfterInvalidUpdate();

            when(userService.updateSpecificUser(testUserId, invalidTestUserPrincipal))
                    .thenReturn(invalidTestUserPrincipalResponse);

            String invalidJsonTestUserPrincipal = mapper.writeValueAsString(invalidTestUserPrincipal);

            mockMvc.perform(put("/api/users/{id}", testUserId)
                        .with(user(testUserPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonTestUserPrincipal))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("Validation error(s) found!"));
        }

    }

}
