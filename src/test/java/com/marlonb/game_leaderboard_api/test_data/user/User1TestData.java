package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.user.*;

import java.time.LocalDateTime;

public class User1TestData {

    public static UserEntity sampleUser1Data () {

        var user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("$2a$10$encoded.password.hash");
        return user1;
    }

    public static UserResponseDto sampleUser1Response () {

        return new UserResponseDto(
                sampleUser1Data().getId(),
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword(),
                "USER",
                sampleUser1Data().getCreatedAt()
        );
    }

    public static UserRequestDto sampleUser1Request () {

        return new UserRequestDto(
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword()
        );
    }

    public static UserUpdateDto sampleUser1Update () {

        return new UserUpdateDto(
                sampleUser1Data().getUsername(),
                "User#456"
        );
    }
}
