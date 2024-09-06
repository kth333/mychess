package com.g1.mychess.matchmaking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matchmaking_request")
public class MatchmakingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "rating_deviation", nullable = false)
    private int ratingDeviation;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "matchmaking_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchmakingStatus status = MatchmakingStatus.WAITING;

    public enum MatchmakingStatus {
        WAITING,
        MATCHED,
        IN_PROGRESS,
        COMPLETED
    }
}
