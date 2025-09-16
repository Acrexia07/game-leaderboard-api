package com.marlonb.game_leaderboard_api.model;

import com.marlonb.game_leaderboard_api.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "player_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    private String playerName;

    private Integer scores;

    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @PrePersist
    public void generateUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        this.timestamp = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.timestamp = LocalDateTime.now();
    }

}
