package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.exception.custom.DuplicateResourceFoundException;
import com.marlonb.game_leaderboard_api.exception.custom.ResourceNotFoundException;
import com.marlonb.game_leaderboard_api.model.user.*;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import com.marlonb.game_leaderboard_api.test_data.user.AdminUser1TestData;
import com.marlonb.game_leaderboard_api.test_data.user.AdminUser2TestData;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import com.marlonb.game_leaderboard_api.test_data.user.User2TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static com.marlonb.game_leaderboard_api.test_assertions.UserTestAssertions.assertUserServiceReturnedExpectedResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserService userService;

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should create user successfully")
        void shouldCreateUserSuccessfully () {

            UserEntity testUser = User1TestData.sampleUser1Data();
            UserResponseDto expectedResponse = User1TestData.sampleUser1Response();
            UserRequestDto testUserRequest = User1TestData.sampleUser1Request();

            when(userMapper.toEntity(any(UserRequestDto.class)))
                    .thenReturn(testUser);

            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(testUser);

            when(userMapper.toResponse(any(UserEntity.class)))
                    .thenReturn(expectedResponse);

            UserResponseDto actualResponse = userService.createUser(testUserRequest);

            assertUserServiceReturnedExpectedResponse(actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("Should create admin user successfully")
        void shouldCreateAdminUserSuccessfully () {

            UserEntity testAdminUser = AdminUser1TestData.sampleAdminUser1Data();
            AdminUserRequestDto testAdminUserRequest = AdminUser1TestData.sampleAdminUser1Request();
            UserResponseDto testAdminUserResponse = AdminUser1TestData.sampleAdminUser1Response();

            when(userMapper.toEntity(any(AdminUserRequestDto.class)))
                    .thenReturn(testAdminUser);

            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(testAdminUser);

            when(userMapper.toResponse(testAdminUser))
                    .thenReturn(testAdminUserResponse);

            UserResponseDto actualResponse = userService.createAdminUser(testAdminUserRequest);

            assertUserServiceReturnedExpectedResponse(actualResponse, testAdminUserResponse);
        }

        @Test
        @DisplayName("Should retrieve all users successfully")
        void shouldRetrieveAllUsersSuccessfully () {

            UserEntity testUser1 = User1TestData.sampleUser1Data();
            UserEntity testUser2 = User2TestData.sampleUser2Data();

            UserResponseDto testUserResponse1 = User1TestData.sampleUser1Response();
            UserResponseDto testUserResponse2 = User2TestData.sampleUser2Response();

            List<UserEntity> listOfUsers = List.of(testUser1, testUser2);
            List<UserResponseDto> expectedResponses = List.of(testUserResponse1, testUserResponse2);

            when(userRepository.findAll()).thenReturn(listOfUsers);

            when(userMapper.toResponse(testUser1)).thenReturn(testUserResponse1);
            when(userMapper.toResponse(testUser2)).thenReturn(testUserResponse2);

            List<UserResponseDto> actualResponses = userService.retrieveAllUsers();

            assertUserServiceReturnedExpectedResponse(actualResponses, expectedResponses);
        }

        @Test
        @DisplayName("Should retrieve specific user successfully")
        void shouldRetrieveSpecificUserSuccessfully () {

            UserEntity testUser = User1TestData.sampleUser1Data();
            final long testUserId = testUser.getId();

            UserResponseDto expectedResponse = User1TestData.sampleUser1Response();

            when(userRepository.findById(testUserId))
                    .thenReturn(Optional.of(testUser));

            when(userMapper.toResponse(testUser))
                    .thenReturn(expectedResponse);

            UserResponseDto actualResponse = userService.retrieveSpecificUser(testUserId);

            assertUserServiceReturnedExpectedResponse(actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("Should update specific user successfully")
        void shouldUpdateSpecificUserSuccessfully () {

            UserEntity testUser = User1TestData.sampleUser1Data();
            final long testUserId = testUser.getId();

            UserUpdateDto testUserUpdate = User1TestData.sampleUser1Update();
            UserEntity testUserAfterUpdate = User1TestData.sampleUserDataAfterUpdate();

            UserResponseDto expectedResponse = User1TestData.sampleUser1Response();

            when(userRepository.findById(testUserId))
                    .thenReturn(Optional.of(testUser));

            doNothing().when(userMapper)
                    .toUpdateFromEntity(testUser, testUserUpdate);

            when(userRepository.save(any(UserEntity.class)))
                    .thenReturn(testUserAfterUpdate);

            when(userMapper.toResponse(any(UserEntity.class)))
                    .thenReturn(expectedResponse);

            UserResponseDto actualResponse = userService.updateSpecificUser(testUserId, testUserUpdate);

            assertUserServiceReturnedExpectedResponse(actualResponse, expectedResponse);
        }

        @Test
        @DisplayName("Should delete specific user successfully")
        void shouldDeleteSpecificUserSuccessfully () {

            UserEntity testUser = User1TestData.sampleUser1Data();
            final long testUserId = testUser.getId();

            when(userRepository.findById(testUserId))
                    .thenReturn(Optional.of(testUser));

            userService.deleteSpecificUser(testUserId);

            verify(userRepository).deleteById(testUserId);
        }

        @Test
        @DisplayName("Should verify user successfully")
        void shouldVerifyUserSuccessfully () {

            LoginRequestDto testUserLoginRequest = User1TestData.sampleUser1LoginRequest();
            Authentication mockAuth = mock(Authentication.class);
            String expectedToken = "jwt-token";

            when(authManager.authenticate(any()))
                    .thenReturn(mockAuth);

            when(jwtService.generateToken(testUserLoginRequest.username()))
                    .thenReturn(expectedToken);

            String result = userService.verifyUser(testUserLoginRequest);

            assertEquals(expectedToken, result);

        }
    }

    @Nested
    class NegativeTests {

        @Nested
        class CrudOperations {

            @Test
            @DisplayName("Should fail when user id does not exist")
            void shouldFailWhenUserIdDoesNotExist () {

                final long nonExistentId = 100L;

                when(userRepository.findById(nonExistentId))
                        .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                             () -> userService.findUserId(nonExistentId));

                verify(userRepository).findById(nonExistentId);
            }

            @Test
            @DisplayName("Should fail to create public user when username already exist")
            void shouldFailToCreatePublicUserWhenUsernameAlreadyExist () {

                UserEntity testUser = User1TestData.sampleUser1Data();
                UserRequestDto testUserRequest = User1TestData.sampleUser1Request();

                when(userMapper.toEntity(any(UserRequestDto.class)))
                        .thenReturn(testUser);

                when(userRepository.existsByUsername("user1"))
                        .thenReturn(true);

                assertThrows(DuplicateResourceFoundException.class,
                             () -> userService.createUser(testUserRequest));

                verify(userRepository, never()).save(any());
            }

            @Test
            @DisplayName("Should fail to create admin user when username already exist")
            void shouldFailToCreateAdminUserWhenUsernameAlreadyExist () {

                UserEntity testAdminUser = AdminUser1TestData.sampleAdminUser1Data();
                AdminUserRequestDto testAdminUserRequest = AdminUser1TestData.sampleAdminUser1Request();

                when(userMapper.toEntity(any(AdminUserRequestDto.class)))
                        .thenReturn(testAdminUser);

                when(userRepository.existsByUsername("admin1")).thenReturn(true);

                assertThrows(DuplicateResourceFoundException.class,
                        () -> userService.createAdminUser(testAdminUserRequest));

                verify(userRepository, never()).save(any());
            }

            @Test
            @DisplayName("Should fail update when username already exist")
            void shouldFailUpdateWhenUsernameAlreadyExist () {

                UserEntity testUser2 = User2TestData.sampleUser2Data();
                final long testUserId = testUser2.getId();

                UserUpdateDto testUserUpdate = User2TestData.sampleUser2Update();
                testUser2.setUsername("user1");

                when(userRepository.findById(testUserId))
                        .thenReturn(Optional.of(testUser2));

                when(userRepository.existsByUsername("user1"))
                        .thenReturn(true);

                assertThrows(DuplicateResourceFoundException.class,
                             () -> userService.updateSpecificUser(testUserId, testUserUpdate));

                verify(userRepository, never()).save(any());
            }
        }
    }
}
