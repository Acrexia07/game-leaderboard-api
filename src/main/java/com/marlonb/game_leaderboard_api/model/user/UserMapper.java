package com.marlonb.game_leaderboard_api.model.user;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import com.marlonb.game_leaderboard_api.model.PlayerInfoMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = PlayerInfoMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface UserMapper {

    @Mapping(target = "playerAccount", source = "player", qualifiedByName = "userToSummary")
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

    default PlayerEntity fromPlayerId (Long playerId) {

        if (playerId == null) return null;

        PlayerEntity player = new PlayerEntity();
        player.setId(playerId);

        return player;
    }
}
