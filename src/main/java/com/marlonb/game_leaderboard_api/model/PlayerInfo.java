package com.marlonb.game_leaderboard_api.model;

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
public class PlayerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private UUID uuid;

    private String playerName;

    private int scores;

    private LocalDateTime timestamp;
}
