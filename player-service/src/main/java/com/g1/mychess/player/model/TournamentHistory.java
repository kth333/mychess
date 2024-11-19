package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Entity class representing a player's tournament history.
 * Stores information about the tournaments a player has participated in, including their final rank and points earned.
 */
@Entity
@Table(name = "tournament_history")
public class TournamentHistory {

    /**
     * The unique identifier for the tournament history record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The {@link Profile} representing the player associated with the tournament history.
     * This is a many-to-one relationship, as a player can have multiple tournament histories.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    /**
     * The ID of the tournament in which the player participated.
     */
    private Long tournamentId;

    /**
     * The name of the tournament in which the player participated.
     */
    private String tournamentName;

    /**
     * The final rank achieved by the player in the tournament.
     */
    private int finalRank;

    /**
     * The number of points earned by the player in the tournament.
     */
    private double points;

    /**
     * The date and time when the player participated in the tournament.
     */
    private LocalDateTime dateParticipated;

    // Other fields, getters, and setters...
}