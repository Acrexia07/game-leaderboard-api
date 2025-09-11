package com.marlonb.game_leaderboard_api.service;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.model.user.UserPrincipal;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.marlonb.game_leaderboard_api.service.ServiceErrorMessages.USER_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameUserDetailServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameUserDetailsService gameUserDetailsService;

    @Test
    @DisplayName("Should return user principal when user exists")
    void shouldReturnUserPrincipalWhenUserExists () {

        String username = "testUser";
        UserEntity mockUser = User1TestData.sampleUser1Data();

        mockUser.setUsername(username);

        when(userRepository.findByUsername(username))
                .thenReturn(mockUser);

        UserDetails actualResult = gameUserDetailsService.loadUserByUsername(username);

        assertNotNull(actualResult);
        assertEquals(username, actualResult.getUsername());
        assertThat(actualResult).isInstanceOf(UserPrincipal.class);
    }

    @Test
    @DisplayName("Should throw username not found exception when user not found")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound () {

        String nonExistentUsername = "unknown";

        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> gameUserDetailsService.loadUserByUsername(nonExistentUsername));

        assertEquals(exception.getMessage(), USER_NOT_FOUND.getErrorMessage());
    }
}
