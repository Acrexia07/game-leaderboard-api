package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.user.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public class User1TestData {

    public static UserEntity sampleUser1Data () {

        var user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("$2a$10$H7Dmx3aWm6z7PFEyjpcKzeGHPP26q00B9sNWZ35hbeqEEvkshg4qC");
        user1.setRole(UserRoles.USER);
        user1.setCreatedAt(LocalDateTime.now());
        return user1;
    }

    public static UserEntity sampleUserDataAfterUpdate () {

        var updatedUser = new UserEntity();
        updatedUser.setId(1L);
        updatedUser.setUsername("user1");
        updatedUser.setPassword("$2a$10$H7Dmx3aWm6z7PFEyjpcKzeGHPP26q00B9sNWZ35hbeqEEvkshg4qC");
        updatedUser.setRole(UserRoles.USER);
        updatedUser.setCreatedAt(LocalDateTime.now());
        return updatedUser;
    }

    public static UserResponseDto sampleUser1Response () {

        return new UserResponseDto(
                sampleUser1Data().getId(),
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword(),
                sampleUser1Data().getCreatedAt()
        );
    }

    public static UserRequestDto sampleUser1Request () {

        return new UserRequestDto(
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword()
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
                "$2a$10$c7vjfX1qO5WQCMChFYweAue8lMBncf4kcX6y2Fnl34kwRLjIpG3Pu"
        );
    }
}
