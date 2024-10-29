package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournament_history")
public class TournamentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    private Long tournamentId;
    private String tournamentName;
    private int finalRank;
    private double points;
    private LocalDateTime dateParticipated;

    // Other fields, getters, and setters...
}