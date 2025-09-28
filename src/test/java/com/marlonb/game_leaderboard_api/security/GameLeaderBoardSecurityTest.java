package com.marlonb.game_leaderboard_api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerResponseDto;
import com.marlonb.game_leaderboard_api.model.user.*;
import com.marlonb.game_leaderboard_api.repository.UserRepository;
import com.marlonb.game_leaderboard_api.service.GameUserDetailsService;
import com.marlonb.game_leaderboard_api.service.JWTService;
import com.marlonb.game_leaderboard_api.service.PlayerService;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;
import com.marlonb.game_leaderboard_api.test_data.user.AdminUser1TestData;
import com.marlonb.game_leaderboard_api.test_data.user.User1TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GameLeaderBoardSecurityTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private PlayerService playerService;

    @MockitoBean
    private GameUserDetailsService gameUserDetailsService;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @BeforeEach
    void setup () {
        userRepository.deleteAll();
        baseUrl = "http://localhost:" + port;
    }

    @Nested
    class PositiveTests {

        @Test
        @DisplayName("Should allow public registration")
        void shouldAllowPublicRegistration () throws JsonProcessingException {

            String url = baseUrl + "/api/users/register";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            UserRequestDto testUserRequest = User1TestData.sampleUser1Request();
            String requestBody = mapper.writeValueAsString(testUserRequest);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        @DisplayName("Should allow valid token to access protected endpoint")
        void shouldAllowValidTokenToAccessProtectedEndpoint () {

            UserPrincipal testUserDetails = User1TestData.sampleUser1PrincipalWithPlayerAccount();
            String testUsername = testUserDetails.getUsername();

            PlayerEntity testPlayer = PlayerTestData.samplePlayerData();
            PlayerResponseDto testPlayerResponse = PlayerTestData.samplePlayerResponse(testPlayer);

            when(gameUserDetailsService.loadUserByUsername(testUsername))
                    .thenReturn(testUserDetails);

            when(playerService.retrieveSpecificPlayerData(testPlayer.getId()))
                    .thenReturn(testPlayerResponse);

            String validToken = jwtService.generateToken(testUserDetails.getUsername());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(validToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = baseUrl + "/api/players/me";

            assertDoesNotThrow(() -> {
                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.GET, entity, String.class
                );

                assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                assertNotEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            });
        }

        @Test
        @DisplayName("Should allow admin only on admin-protected endpoint")
        void shouldAllowAdminOnlyOnAdminProtectedEndpoint () {

            UserEntity testAdmin = AdminUser1TestData.sampleAdminUser1Data();
            UserPrincipal adminPrincipal = AdminUser1TestData.sampleAdmin1Principal();

            when(gameUserDetailsService.loadUserByUsername(testAdmin.getUsername()))
                    .thenReturn(adminPrincipal);

            String validToken = jwtService.generateToken(testAdmin.getUsername());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(validToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = baseUrl + "/api/players";

            assertDoesNotThrow(() -> {
                ResponseEntity<String> response = restTemplate.exchange(
                      url, HttpMethod.GET, entity,  String.class
                );

                assertNotEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            });
        }
    }
}
