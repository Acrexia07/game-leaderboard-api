package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.user.*;

public class User2TestData {

    public static UserEntity sampleUser2Data () {

        var user2 = new UserEntity();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("$2a$11$encoded.password.hash");
        user2.setRoles(UserRoles.USER);
        return user2;
    }

    public static UserResponseDto sampleUser2Response () {

        return new UserResponseDto(
                sampleUser2Data().getId(),
                sampleUser2Data().getUsername(),
                sampleUser2Data().getPassword(),
                sampleUser2Data().getRoles(),
                sampleUser2Data().getCreatedAt()
        );
    }

    public static UserRequestDto sampleUser2Request () {

        return new UserRequestDto(
                sampleUser2Data().getUsername(),
                sampleUser2Data().getPassword(),
                sampleUser2Data().getRoles()
        );
    }

    public static UserUpdateDto sampleUser2Update () {

        return new UserUpdateDto(
                sampleUser2Data().getUsername(),
                "User#789",
                sampleUser2Data().getRoles()
        );
    }

}
