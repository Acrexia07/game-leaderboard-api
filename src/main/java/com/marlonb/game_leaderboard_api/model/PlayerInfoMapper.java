package com.marlonb.game_leaderboard_api.model;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface PlayerInfoMapper {

    // Response mapping
    PlayerResponseDto toResponse (PlayerEntity playerInfo);

    // Request mapping
    @Mapping(target = "id", ignore = true)
    PlayerEntity toEntity (PlayerRequestDto playerRequest);

    // Update mapping
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateFromEntity (@MappingTarget PlayerEntity playerInfo, PlayerUpdateDto playerUpdate);
}
