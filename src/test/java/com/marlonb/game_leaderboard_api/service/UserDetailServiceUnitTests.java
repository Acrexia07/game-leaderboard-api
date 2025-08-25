package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserMapper;
import com.marlonb.game_leaderboard_api.model.user.UserRequestDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.marlonb.game_leaderboard_api.test_assertions.PlayerTestAssertions.assertServiceReturnedExpectedResponse;
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

    }
}
