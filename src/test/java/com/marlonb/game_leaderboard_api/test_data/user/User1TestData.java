package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.user.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class User1TestData {

    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static UserEntity sampleUser1Data () {

        var user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword(encoder.encode("Test@123"));
        user1.setRole(UserRoles.USER);
        user1.setCreatedAt(LocalDateTime.now());
        return user1;
    }

    /* --- USER PRINCIPAL DATA --- */
    public static UserPrincipal sampleUser1PrincipalData () {

        return new UserPrincipal(
                sampleUser1Data()
        );
    }

    public static UserResponseDto sampleUser1PrincipalResponse () {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1PrincipalData().getUsername(),
                sampleUser1PrincipalData().getPassword(),
                sampleUser1Data().getCreatedAt()
        );
    }

    public static UserUpdateDto sampleUser1PrincipalUpdate () {

        return new UserUpdateDto(
                "User01",
                "Test@123"
        );
    }

    public static UserUpdateDto sampleUser1PrincipalInvalidUpdate () {

        return new UserUpdateDto(
                "User01",
                "Test#4567890abcdefghjklmn"
        );
    }

    public static UserResponseDto sampleUser1PrincipalResponseAfterUpdate() {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1PrincipalUpdate().getUsername(),
                sampleUser1PrincipalUpdate().getPassword(),
                sampleUser1Data().getCreatedAt()
        );
    }

    public static UserResponseDto sampleUser1PrincipalResponseAfterInvalidUpdate() {

        return new UserResponseDto(
                sampleUser1PrincipalData().getId(),
                sampleUser1PrincipalInvalidUpdate().getUsername(),
                sampleUser1PrincipalInvalidUpdate().getPassword(),
                sampleUser1Data().getCreatedAt()
        );
    }

    /* --- LOGIN DATA --- */
    public static LoginRequestDto sampleUser1LoginData () {

        return new LoginRequestDto(
                sampleUser1Data().getUsername(),
                sampleUser1Data().getPassword()
        );
    }

    public static LoginRequestDto sampleUser1InvalidLoginData () {

        return new LoginRequestDto(
                sampleUser1Data().getUsername(),
                "WrongPassword"
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
                sampleUser1Data().getCreatedAt()
        );
    }

    public static UserRequestDto sampleUser1Request () {

        return new UserRequestDto(
                sampleUser1Data().getUsername(),
                "Tester@123"
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
