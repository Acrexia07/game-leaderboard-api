package com.marlonb.game_leaderboard_api.test_assertions;

import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.user.UserResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestAssertions {

    public static void assertUserServiceReturnedExpectedResponse (UserResponseDto actualResponse,
                                                                  UserResponseDto expectedResponse) {

        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
    }

    public static void assertUserServiceReturnedExpectedResponse (List<UserResponseDto> actualResponse,
                                                                  List<UserResponseDto> expectedResponse) {

        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
    }
}
