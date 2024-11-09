package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_history")
public class MatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    private Long matchId;
    private Long tournamentId;
    private String opponent;
    private String result; // WIN, LOSS, DRAW
    private LocalDateTime datePlayed;

    // Other fields, getters, and setters...
}
