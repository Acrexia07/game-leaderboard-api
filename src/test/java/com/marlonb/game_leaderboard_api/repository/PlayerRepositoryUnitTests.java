package com.marlonb.game_leaderboard_api.repository;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.test_data.Player2TestData;
import com.marlonb.game_leaderboard_api.test_data.Player3TestData;
import com.marlonb.game_leaderboard_api.test_data.Player4TestData;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PlayerRepositoryUnitTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    private PlayerEntity testPlayer;
    private PlayerEntity testPlayer2;
    private PlayerEntity testPlayer3;
    private PlayerEntity testPlayer4;

    @BeforeEach
    void setup () {
        testPlayer = PlayerTestData.samplePlayerDataWithoutID();
        testPlayer2 = Player2TestData.sampleIncompletePlayer2Data();
        testPlayer3 = Player3TestData.sampleIncompletePlayer3Data();
        testPlayer4 = Player4TestData.sampleIncompletePlayer4Data();

        entityManager.clear();
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should return true if player name exists")
        void shouldReturnTrueIfPlayerNameExists () {

            entityManager.persist(testPlayer);

            Boolean exists = playerRepository.existsByPlayerName("Player1");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("Should return list of responses of top 3 players successfully")
        void shouldReturnListOfResponsesOfTop3PlayersSuccessfully () {

            List<PlayerEntity> players = List.of(testPlayer, testPlayer2, testPlayer3, testPlayer4);
            playerRepository.saveAll(players);

            List<PlayerEntity> topPlayers = playerRepository.findTop3PlayerByOrderByScoresDescAndTimestampAsc();

            // Verifies that the list is not null, contains exactly 3 players,
            // and is sorted by scores descending, with timestamp as a tiebreaker
            assertThat(topPlayers).isNotNull()
                                  .hasSize(3)
                                  .isSortedAccordingTo(
                                          Comparator.comparing(PlayerEntity::getScores).reversed()
                                                    .thenComparing(PlayerEntity::getTimestamp)
                                  );
        }

        @Test
        @DisplayName("Should order players by timestamp when scores are the same")
        void shouldOrderPlayersByTimestampWhenScoresAreTheSame () {

            List<PlayerEntity> players = List.of(testPlayer, testPlayer2, testPlayer3, testPlayer4);
            players.forEach(p -> p.setScores(100));
            playerRepository.saveAll(players);

            List<PlayerEntity> topPlayers = playerRepository.findTop3PlayerByOrderByScoresDescAndTimestampAsc();

            assertThat(topPlayers).isNotNull()
                    .hasSize(3)
                    .extracting(PlayerEntity::getPlayerName)
                    .containsExactly("Player1","Player2","Player4");
        }
    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("Should return false if player name does not exists")
        void shouldReturnFalseIfPlayerNameDoesNotExists () {

            Boolean exists = playerRepository.existsByPlayerName("NonExistent");

            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("Should return empty if there are no players")
        void shouldReturnEmptyIfThereAreNoPlayers (){

            List<PlayerEntity> topPlayers = playerRepository.findTop3PlayerByOrderByScoresDescAndTimestampAsc();

            assertThat(topPlayers).isNotNull()
                                  .isEmpty();
        }

        @Test
        @DisplayName("Should handle fewer than 3 players")
        void shouldHandleFewerThan3Players () {

            List<PlayerEntity> fewerTopPlayers = List.of(testPlayer, testPlayer2);
            playerRepository.saveAll(fewerTopPlayers);

            List<PlayerEntity> topPlayers = playerRepository.findTop3PlayerByOrderByScoresDescAndTimestampAsc();

            assertThat(topPlayers).isNotNull()
                    .hasSize(2)
                    .isSortedAccordingTo(
                            Comparator.comparing(PlayerEntity::getScores).reversed()
                                    .thenComparing(PlayerEntity::getTimestamp)
                    );
        }
    }
}
