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

    // Custom Response Mapping
    @Named("userToSummary")
    @Mapping(target = "playerName", source = "playerName")
    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "scores", source = "scores")
    @Mapping(target = "gameRank", source = "gameRank")
    PlayerSummaryDto toSummary (PlayerEntity player);

    // Request mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PlayerEntity toEntity (PlayerRequestDto playerRequest);

    // Update mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playerName", source = "playerName")
    @Mapping(target = "scores", source = "scores")
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
