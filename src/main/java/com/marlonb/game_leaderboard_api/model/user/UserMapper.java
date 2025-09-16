package com.marlonb.game_leaderboard_api.model.user;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface UserMapper {

    UserResponseDto toResponse (UserEntity userResponse);

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity (UserRequestDto userRequest);

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity (AdminUserRequestDto adminRequest);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateFromEntity (@MappingTarget UserEntity user, UserUpdateDto userUpdate);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateFromEntity (@MappingTarget UserEntity user, AdminUserUpdateDto adminUpdate);

    default UserEntity fromUserId(Long userId) {

        if (userId == null) return null;

        UserEntity user = new UserEntity();
        user.setId(userId);

        return user;
    }
}
