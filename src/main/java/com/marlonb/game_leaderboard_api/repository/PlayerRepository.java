package com.marlonb.game_leaderboard_api.repository;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    boolean existsByPlayerName (String playerName);
}
