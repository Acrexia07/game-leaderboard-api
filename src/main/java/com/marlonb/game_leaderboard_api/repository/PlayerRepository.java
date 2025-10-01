package com.marlonb.game_leaderboard_api.repository;

import com.marlonb.game_leaderboard_api.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    boolean existsByPlayerName (String playerName);

    @Query(value = """
                SELECT p.id, p.uuid, p.player_name, p.scores, p.timestamp, p.user_id,
                       ROW_NUMBER() OVER (ORDER BY p.scores DESC, p.timestamp ASC) as game_rank
                FROM player_data p
                ORDER BY p.scores DESC, p.timestamp ASC
                LIMIT 3
                """, nativeQuery = true)
    List<PlayerEntity> findTop3PlayerByOrderByScoresDescAndTimestampAsc ();
}
