package com.marlonb.game_leaderboard_api.model;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface PlayerInfoMapper {

    // Response mapping
    @Mapping(target = "userId", source = "user.id")
    PlayerResponseDto toResponse (PlayerEntity playerInfo);

    // Request mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PlayerEntity toEntity (PlayerRequestDto playerRequest);

    // Update mapping
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateFromEntity (@MappingTarget PlayerEntity playerInfo, PlayerUpdateDto playerUpdate);

    default PlayerEntity fromPlayerId (Long playerId) {

        if (playerId == null) return null;

        PlayerEntity player = new PlayerEntity();
        player.setId(playerId);

        return player;
    }

    // For UserEntity lookups
    default UserEntity fromUserId(Long userId) {

        if (userId == null) return null;

        UserEntity user = new UserEntity();
        user.setId(userId);

        return user;
    }
}
