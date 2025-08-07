package com.marlonb.game_leaderboard_api.test_assertions;

import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

public class PlayerTestAssertions {

    public static void assertServiceReturnedExpectedResponse (PlayerResponseDto actualResponse,
                                                              PlayerResponseDto expectedResponse) {

        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
    }

    public static void assertServiceReturnedExpectedResponse (List<PlayerResponseDto> actualResponse,
                                                              List<PlayerResponseDto> expectedResponse) {

        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
    }
}
