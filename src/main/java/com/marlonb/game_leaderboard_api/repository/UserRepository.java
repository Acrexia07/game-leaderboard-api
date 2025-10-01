package com.marlonb.game_leaderboard_api.repository;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername (String username);
    boolean existsByUsername (String username);
}
