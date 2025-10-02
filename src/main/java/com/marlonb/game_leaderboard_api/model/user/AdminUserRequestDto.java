package com.marlonb.game_leaderboard_api.model.user;

import com.marlonb.game_leaderboard_api.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserRequestDto {

    @NotNull(message = "username is required!")
    @Size(min = 2, max = 6, message = "Username must be between 2 and 6 characters")
    private String username;

    @NotNull(message = "password is required and cannot be null!")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    @NotNull(message = "role is required!")
    @ValueOfEnum(enumClass = UserRoles.class, message = "Role must be ADMIN or USER")
    private UserRoles role;
}
