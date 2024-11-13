package com.g1.mychess.match.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a match within a tournament, containing details such as participants,
 * scheduled time, status, and round number.
 */
@Entity
@Table(name = "matches")
public class Match {

    /**
     * The unique identifier for the match.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique identifier for the tournament to which this match belongs.
     */
    @Column(name = "tournament_id", nullable = false)
    @NotNull
    private Long tournamentId;

    /**
     * A set of participants (MatchPlayer instances) involved in this match.
     * The relationship is managed by the MatchPlayer entity.
     */
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchPlayer> participants;

    /**
     * The scheduled time for the match, which cannot be null.
     */
    @Column(name = "scheduled_time", nullable = false)
    @NotNull
    private LocalDateTime scheduledTime;

    /**
     * The current status of the match, which can be SCHEDULED, ONGOING, COMPLETED, or CANCELED.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private MatchStatus status;

    /**
     * The round number of the match within the tournament.
     * Must be a positive integer if specified.
     */
    @Column(name = "round_number")
    @Min(1)
    private Integer roundNumber;

    /**
     * Enumeration representing the possible statuses of a match.
     */
    public enum MatchStatus {
        SCHEDULED,
        ONGOING,
        COMPLETED,
        CANCELED
    }

    // Getters and Setters

    /**
     * Gets the unique ID of the match.
     * @return the match ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID for the match.
     * @param id the match ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the tournament ID to which this match belongs.
     * @return the tournament ID.
     */
    public Long getTournamentId() {
        return tournamentId;
    }

    /**
     * Sets the tournament ID to which this match belongs.
     * @param tournamentId the tournament ID to set.
     */
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Gets the set of participants (MatchPlayer instances) involved in the match.
     * @return the set of participants.
     */
    public Set<MatchPlayer> getParticipants() {
        return participants;
    }

    /**
     * Sets the set of participants (MatchPlayer instances) involved in the match.
     * @param participants the participants to set.
     */
    public void setParticipants(Set<MatchPlayer> participants) {
        this.participants = participants;
    }

    /**
     * Gets the scheduled time of the match.
     * @return the scheduled time.
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Sets the scheduled time of the match.
     * @param scheduledTime the scheduled time to set.
     */
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    /**
     * Gets the current status of the match.
     * @return the match status.
     */
    public MatchStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the match.
     * @param status the match status to set.
     */
    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    /**
     * Gets the round number of the match within the tournament.
     * @return the round number.
     */
    public Integer getRoundNumber() {
        return roundNumber;
    }

    /**
     * Sets the round number of the match within the tournament.
     * @param roundNumber the round number to set.
     */
    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }
}
