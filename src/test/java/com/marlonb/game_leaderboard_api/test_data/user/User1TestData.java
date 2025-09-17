package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.PlayerSummaryDto;
import com.marlonb.game_leaderboard_api.model.user.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static com.marlonb.game_leaderboard_api.test_data.PlayerTestData.samplePlayerData;

public class User1TestData {

    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String RAW_USERNAME = "user1";
    private static final String RAW_PASSWORD = "Test@123";

    private static final String RAW_UPDATED_USERNAME = "User01";

    private static final String RAW_INVALID_UPDATED_PASSWORD = "Test#4567890abcdefghjklmn";
    private static final String RAW_INVALID_LOGIN_PASSWORD = "WrongPassword";

    private static final String ENCODED_PASSWORD = encoder.encode(RAW_PASSWORD);

    private final static UserEntity BASE_USER;
    private final static UserPrincipal BASE_PRINCIPAL;

    static {
        BASE_USER = new UserEntity();
        BASE_USER.setId(1L);
        BASE_USER.setUsername(RAW_USERNAME);
        BASE_USER.setPassword(RAW_PASSWORD);
        BASE_USER.setRole(UserRoles.USER);
        BASE_USER.setCreatedAt(LocalDateTime.now());

        BASE_PRINCIPAL = new UserPrincipal(BASE_USER);
    }

    public static UserEntity sampleUser1Data () {
        return BASE_USER;
    }

    /* --- USER PRINCIPAL DATA --- */
    public static UserPrincipal sampleUser1PrincipalData () {
        return BASE_PRINCIPAL;
    }

    public static PlayerSummaryDto playerSummaryDto () {

        return new PlayerSummaryDto(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getUuid(),
                samplePlayerData().getScores(),
                samplePlayerData().getTimestamp()
        );
    }

    public static UserResponseDto sampleUser1PrincipalResponse () {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1PrincipalData().getUsername(),
                sampleUser1PrincipalData().getPassword(),
                sampleUser1Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    public static UserUpdateDto sampleUser1PrincipalUpdate () {

        return new UserUpdateDto(
                RAW_UPDATED_USERNAME,
                RAW_PASSWORD
        );
    }

    public static UserUpdateDto sampleUser1PrincipalInvalidUpdate () {

        return new UserUpdateDto(
                RAW_UPDATED_USERNAME,
                RAW_INVALID_UPDATED_PASSWORD
        );
    }

    public static UserResponseDto sampleUser1PrincipalResponseAfterUpdate() {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1PrincipalUpdate().getUsername(),
                sampleUser1PrincipalUpdate().getPassword(),
                sampleUser1Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    public static UserResponseDto sampleUser1PrincipalResponseAfterInvalidUpdate() {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1PrincipalInvalidUpdate().getUsername(),
                sampleUser1PrincipalInvalidUpdate().getPassword(),
                sampleUser1Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    /* --- LOGIN DATA --- */
    public static LoginRequestDto sampleUser1LoginData () {

        return new LoginRequestDto(
                BASE_USER.getUsername(),
                RAW_PASSWORD
        );
    }

    public static LoginRequestDto sampleUser1InvalidLoginData () {

        return new LoginRequestDto(
                sampleUser1Data().getUsername(),
                RAW_INVALID_LOGIN_PASSWORD
        );
    }

    public static UserEntity sampleUserDataAfterUpdate () {

        var updatedUser = new UserEntity();
        updatedUser.setId(1L);
        updatedUser.setUsername("user1");
        updatedUser.setPassword(encoder.encode("Test#123"));
        updatedUser.setRole(UserRoles.USER);
        updatedUser.setCreatedAt(LocalDateTime.now());
        return updatedUser;
    }

    public static UserResponseDto sampleUser1Response () {

        return new UserResponseDto(
                sampleUser1Data().getId(),
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword(),
                sampleUser1Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    public static UserRequestDto sampleUser1Request () {

        return new UserRequestDto(
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword()
        );
    }

    public static UserRequestDto sampleInvalidUser1Request () {

        return new UserRequestDto(
                "user2541asdfadf",
                encoder.encode("Test@123")
        );
    }

    public static LoginRequestDto sampleUser1LoginRequest () {

        return new LoginRequestDto(
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword()
        );
    }

    public static UserUpdateDto sampleUser1Update () {

        return new UserUpdateDto(
                sampleUser1Data().getUsername(),
                encoder.encode("Test@123")
        );
    }
}
