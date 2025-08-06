package com.marlonb.game_leaderboard_api.test_assertions;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

public class PlayerTestAssertions {

    public static void playerAssertRecursionComparisons (PlayerEntity player,
                                                  PlayerResponseDto actualResponse,
                                                  PlayerResponseDto expectedResponse) {

        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
        assertThat(player.getUuid()).isEqualTo(actualResponse.uuid());
    }

    public static void playerAssertRecursionComparisons (PlayerEntity player1,
                                                         PlayerEntity player2,
                                                         List<PlayerResponseDto> actualResponse,
                                                         List<PlayerResponseDto> expectedResponse) {

        assertThat(actualResponse).usingRecursiveAssertion().isEqualTo(expectedResponse);
        assertThat(player1.getUuid()).usingRecursiveAssertion().isEqualTo(actualResponse.getFirst().uuid());
        assertThat(player2.getUuid()).usingRecursiveAssertion().isEqualTo(actualResponse.getLast().uuid());
    }
}
