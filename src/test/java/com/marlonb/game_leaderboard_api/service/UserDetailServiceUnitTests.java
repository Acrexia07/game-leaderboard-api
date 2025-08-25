package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserMapper;
import com.marlonb.game_leaderboard_api.model.user.UserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import com.marlonb.game_leaderboard_api.test_data.user.User2TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.marlonb.game_leaderboard_api.test_assertions.UserTestAssertions.assertUserServiceReturnedExpectedResponse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

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

            List<UserResponseDto> actualResponse = userService.retrieveAllUsers();

            assertUserServiceReturnedExpectedResponse(actualResponse, expectedResponses);
        }
    }
}
