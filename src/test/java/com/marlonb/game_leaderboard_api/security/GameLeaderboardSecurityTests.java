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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GameLeaderboardSecurityTests {

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

        @Test
        @DisplayName("Should authenticated user access their own resource")
        void shouldAuthenticatedUserAccessTheirOwnResource () {

            UserResponseDto testUserResponse = User1TestData.sampleUser1PrincipalResponse();
            UserPrincipal userPrincipal = PlayerTestData.sampleUserPrincipal();

            when(gameUserDetailsService.loadUserByUsername(testUserResponse.username()))
                    .thenReturn(userPrincipal);

            String validToken = jwtService.generateToken(testUserResponse.username());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(validToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = baseUrl + "/api/players/me";

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );

            assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    class NegativeTests {

        @Test
        @DisplayName("Should reject invalid JWT")
        void shouldRejectInvalidJwt () {

            String invalidToken = "not-jwt-at-all";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(invalidToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = baseUrl + "/api/users/me";

            HttpClientErrorException exception =
                    assertThrows(HttpClientErrorException.class, () -> {
                        restTemplate.exchange(
                                url, HttpMethod.GET, entity, String.class
                        );
                    });

            assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        }

        @Test
        @DisplayName("Should reject expired JWT")
        void shouldRejectExpiredJwt () {

            String secretKey = "mySampleTestKeyForThisTestAboutExpirationKey";
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

            String expiredToken = Jwts.builder()
                                      .subject("user1")
                                    .issuedAt(Date.from(Instant.now().minusSeconds(3600)))
                                    .expiration(Date.from(Instant.now().minusSeconds(1800)))
                                    .signWith(key)
                                    .compact();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(expiredToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = baseUrl + "/api/users/me";

            HttpClientErrorException exception =
                    assertThrows(HttpClientErrorException.class, () -> {
                        restTemplate.exchange(
                                url, HttpMethod.GET, entity, String.class
                        );
                    });

            assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        }

        @Test
        @DisplayName("Should reject unauthenticated requests to protected endpoint")
        void shouldRejectUnauthenticatedRequestToProtectedEndpoint () {

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = baseUrl + "/api/users";

            HttpClientErrorException exception =
                    assertThrows(HttpClientErrorException.class, () -> {
                        restTemplate.exchange(
                                url, HttpMethod.GET, entity, String.class
                        );
                    });

            assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        }

        @Test
        @DisplayName("Should reject unauthorized request from protected endpoint")
        void shouldRejectUnauthorizedRequestFromProtectedEndpoint () {

        }
    }
}
