package com.marlonb.game_leaderboard_api.test_data.user;

import com.marlonb.game_leaderboard_api.model.user.*;

import java.time.LocalDateTime;

public class AdminUser1TestData {

    public static UserEntity sampleAdminUser1Data () {

        UserEntity admin1 = new UserEntity();
        admin1.setId(3L);
        admin1.setUsername("admin1");
        admin1.setPassword("Admin@123");
        admin1.setRole(UserRoles.ADMIN);
        admin1.setCreatedAt(LocalDateTime.now());
        return admin1;
    }

    public static UserEntity sampleAdminUser1DataAfterUpdate () {

        UserEntity updatedAdmin = new UserEntity();
        updatedAdmin.setId(3L);
        updatedAdmin.setUsername("admin1");
        updatedAdmin.setPassword("Admin#456");
        updatedAdmin.setRole(UserRoles.ADMIN);
        updatedAdmin.setCreatedAt(LocalDateTime.now());
        return updatedAdmin;
    }

    public static UserResponseDto sampleAdminUser1Response () {

        return new UserResponseDto(
                sampleAdminUser1Data().getId(),
                sampleAdminUser1Data().getUsername(),
                sampleAdminUser1Data().getPassword(),
                sampleAdminUser1Data().getCreatedAt()
        );
    }

    public static AdminUserRequestDto sampleAdminUser1Request () {

        return new AdminUserRequestDto(
                sampleAdminUser1Data().getUsername(),
                sampleAdminUser1Data().getPassword(),
                sampleAdminUser1Data().getRole()
        );
    }

    public static AdminUserUpdateDto sampleAdminUser1Update () {

        return new AdminUserUpdateDto(
                sampleAdminUser1Data().getUsername(),
                "Admin#789",
                sampleAdminUser1Data().getRole()
        );
    }
}
