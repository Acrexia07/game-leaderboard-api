package com.marlonb.game_leaderboard_api.repository;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserControllerIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser1;

    @BeforeEach
    void setup (){
        testUser1 = User1TestData.sampleUser1DataWithoutId();
        entityManager.clear();
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("findByUsername: Should return user when username found")
        void shouldReturnUserWhenUsernameExists () {

            entityManager.persistAndFlush(testUser1);

            UserEntity found = userRepository.findByUsername("User1");

            assertThat(found).isNotNull();
            assertThat(found.getUsername()).isEqualTo("User1");
        }

        @Test
        @DisplayName("existsByUsername: Should return true if username exists")
        void shouldReturnTrueIfUsernameExists () {

            entityManager.persistAndFlush(testUser1);

            Boolean exists = userRepository.existsByUsername("User1");

            assertThat(exists).isTrue();
        }
    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("findByUsername: Should return null when username not found")
        void shouldReturnNullWhenUsernameDoesNotExist () {

            UserEntity found = userRepository.findByUsername("nonExistent");

            assertThat(found).isNull();
        }

        @Test
        @DisplayName("findByUsername: Should return null when username does not exist")
        void shouldReturnFalseWhenUsernameDoesNotExist () {

            Boolean found = userRepository.existsByUsername("nonExistent");

            assertThat(found).isFalse();
        }
    }
}
