package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.user.*;

import java.time.LocalDateTime;

public class AdminUser1TestData {

    private static final UserEntity BASE_ADMIN_DATA;
    private static final UserPrincipal ADMIN_PRINCIPAL;

    /* --- RAW VALUES --- */
    private static final Long ADMIN1_ID = 3L;
    private static final String ADMIN1_NAME = "admin1";
    private static final String ADMIN1_PASSWORD = "Abcd1234@";
    private static final UserRoles ADMIN1_ROLE = UserRoles.ADMIN;
    private static final LocalDateTime ADMIN_CREATION_DATE = LocalDateTime.of
                                                             (2025, 1, 1, 0, 0 ,0);

    /* --- INVALID VALUES --- */
    private static final String INVALID_ADMIN1_NAME = "user2541asdfadf";
    private static final String INVALID_ADMIN1_PASSWORD = "Tester@13asddfioae";

    static {
        BASE_ADMIN_DATA = new UserEntity();
        BASE_ADMIN_DATA.setId(ADMIN1_ID);
        BASE_ADMIN_DATA.setUsername(ADMIN1_NAME);
        BASE_ADMIN_DATA.setPassword(ADMIN1_PASSWORD);
        BASE_ADMIN_DATA.setRole(ADMIN1_ROLE);
        BASE_ADMIN_DATA.setCreatedAt(ADMIN_CREATION_DATE);

        ADMIN_PRINCIPAL = new UserPrincipal(BASE_ADMIN_DATA);
    }

    public static UserEntity sampleAdminUser1Data () {
        return BASE_ADMIN_DATA;
    }

    public static UserPrincipal sampleAdmin1Principal () {
        return ADMIN_PRINCIPAL;
    }

    public static UserResponseDto sampleAdminUser1Response () {

        return new UserResponseDto(
                sampleAdminUser1Data().getId(),
                sampleAdminUser1Data().getUsername(),
                sampleAdminUser1Data().getCreatedAt(),
                null
        );
    }

    public static AdminUserRequestDto sampleAdminUser1Request () {

        return new AdminUserRequestDto(
                ADMIN1_NAME,
                ADMIN1_PASSWORD,
                ADMIN1_ROLE
        );
    }

    public static AdminUserRequestDto sampleAdminUser1InvalidRequest () {

        return new AdminUserRequestDto(
                INVALID_ADMIN1_NAME,
                INVALID_ADMIN1_PASSWORD,
                sampleAdminUser1Data().getRole()
        );
    }
}
