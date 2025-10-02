package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.PlayerSummaryDto;
import com.marlonb.game_leaderboard_api.model.user.*;
import com.marlonb.game_leaderboard_api.test_data.PlayerTestData;

import java.time.LocalDateTime;

import static com.marlonb.game_leaderboard_api.test_data.PlayerTestData.samplePlayerData;

public class User1TestData {

    private static final UserEntity BASE_USER1;
    private static final UserPrincipal BASE_PRINCIPAL;

    /* --- RAW VALUES --- */
    private static final Long USER1_ID = 1L;
    private static final String USER1_NAME = "User1";
    private static final String USER1_PASSWORD = "Passw0rd@";
    private static final UserRoles USER1_ROLE = UserRoles.USER;
    private static final LocalDateTime USER1_CREATION_DATE = LocalDateTime.of
                                                            (2025, 12, 25, 0, 0, 0);

    /* --- UPDATED VALUES --- */
    private static final String UPDATED_USER1_NAME = "User01";
    private static final String UPDATED_USER1_PASSWORD = "Passw0rd@123";

    /* --- INVALID VALUES --- */
    private static final String INVALID_USER1_NAME = "user2541asdfadf";
    private static final String INVALID_USER1_PASSWORD = "Test#4567890abcdefghjklmn";

    static {
        BASE_USER1 = new UserEntity();
        BASE_USER1.setId(USER1_ID);
        BASE_USER1.setUsername(USER1_NAME);
        BASE_USER1.setPassword(USER1_PASSWORD);
        BASE_USER1.setRole(USER1_ROLE);
        BASE_USER1.setCreatedAt(USER1_CREATION_DATE);

        BASE_PRINCIPAL = new UserPrincipal(BASE_USER1);
    }

    /* --- USER DATA --- */
    public static UserEntity sampleUser1Data () {
        return BASE_USER1;
    }

    public static PlayerSummaryDto playerSummaryDto () {

        return new PlayerSummaryDto(
                samplePlayerData().getPlayerName(),
                samplePlayerData().getUuid(),
                samplePlayerData().getScores()
        );
    }

    public static UserUpdateDto sampleUser1InvalidUpdate () {

        return new UserUpdateDto(
                UPDATED_USER1_NAME,
                INVALID_USER1_PASSWORD
        );
    }

    public static UserEntity sampleUserDataAfterUpdate () {

        var updatedUser = new UserEntity();
        updatedUser.setId(USER1_ID);
        updatedUser.setUsername(USER1_NAME);
        updatedUser.setPassword(UPDATED_USER1_PASSWORD);
        updatedUser.setRole(USER1_ROLE);
        updatedUser.setCreatedAt(USER1_CREATION_DATE);
        return updatedUser;
    }

    public static UserResponseDto sampleUser1Response () {

        return new UserResponseDto(
                sampleUser1Data().getId(),
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword(),
                sampleUser1Data().getCreatedAt(),
                null
        );
    }

    public static UserRequestDto sampleUser1Request () {

        return new UserRequestDto(
                USER1_NAME,
                USER1_PASSWORD
        );
    }

    public static UserResponseDto sampleUser1ResponseForCreate () {

        return new UserResponseDto(
                sampleUser1Data().getId(),
                sampleUser1Request().getUsername(),
                sampleUser1Request().getPassword(),
                sampleUser1Data().getCreatedAt(),
                null
        );
    }

    public static UserRequestDto sampleInvalidUser1Request () {

        return new UserRequestDto(
                INVALID_USER1_NAME,
                USER1_PASSWORD
        );
    }

    public static UserUpdateDto sampleUser1Update () {

        return new UserUpdateDto(
                UPDATED_USER1_NAME,
                UPDATED_USER1_PASSWORD
        );
    }

    /* --- LOGIN DATA --- */
    public static LoginRequestDto sampleUser1LoginRequest () {

        return new LoginRequestDto(
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword()
        );
    }

    public static LoginRequestDto sampleUser1InvalidLoginData () {

        return new LoginRequestDto(
                sampleUser1Data().getUsername(),
                INVALID_USER1_PASSWORD
        );
    }

    /* --- USER PRINCIPAL DATA --- */
    public static UserPrincipal sampleUser1PrincipalData () {
        return BASE_PRINCIPAL;
    }

    public static UserPrincipal sampleUser1PrincipalWithPlayerAccount () {
        BASE_USER1.setPlayer(PlayerTestData.samplePlayerData());
        return BASE_PRINCIPAL;
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

    public static UserResponseDto sampleUser1PrincipalResponseAfterUpdate() {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1Update().getUsername(),
                sampleUser1Update().getPassword(),
                sampleUser1Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    public static UserResponseDto sampleUser1ResponseAfterInvalidUpdate() {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1InvalidUpdate().getUsername(),
                sampleUser1InvalidUpdate().getPassword(),
                sampleUser1Data().getCreatedAt(),
                playerSummaryDto()
        );
    }

    /* --- USER DATA FOR INTEGRATION TESTS --- */
    public static UserEntity sampleUser1DataWithoutId () {
        return new UserEntity(
                null,
                USER1_NAME,
                USER1_PASSWORD,
                USER1_ROLE,
                USER1_CREATION_DATE,
                null
        );
    }
}
