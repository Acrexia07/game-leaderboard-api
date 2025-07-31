package com.marlonb.game_leaderboard_api.model;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface PlayerInfoMapper {

    // Response mapping
    PlayerInfoResponse toResponse (PlayerInfo playerInfo);

    // Request mapping
    @Mapping(target = "id", ignore = true)
    PlayerInfo toEntity (PlayerInfoRequest playerRequest);

    // Update mapping
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateFromEntity (@MappingTarget PlayerInfo playerInfo, PlayerInfoUpdate playerUpdate);
}
