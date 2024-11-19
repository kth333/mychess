package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Represents a match history record for a player.
 */
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

    // Getters and Setters

    /**
     * Gets the ID of the match history record.
     *
     * @return the match history record ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the match history record.
     *
     * @param id the new match history record ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the profile associated with this match history.
     *
     * @return the profile.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile associated with this match history.
     *
     * @param profile the new profile.
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Gets the match ID.
     *
     * @return the match ID.
     */
    public Long getMatchId() {
        return matchId;
    }

    /**
     * Sets the match ID.
     *
     * @param matchId the new match ID.
     */
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    /**
     * Gets the tournament ID.
     *
     * @return the tournament ID.
     */
    public Long getTournamentId() {
        return tournamentId;
    }

    /**
     * Sets the tournament ID.
     *
     * @param tournamentId the new tournament ID.
     */
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Gets the opponent's name.
     *
     * @return the opponent's name.
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * Sets the opponent's name.
     *
     * @param opponent the new opponent's name.
     */
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    /**
     * Gets the result of the match.
     *
     * @return the result (WIN, LOSS, DRAW).
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the result of the match.
     *
     * @param result the new result (WIN, LOSS, DRAW).
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Gets the date the match was played.
     *
     * @return the date the match was played.
     */
    public LocalDateTime getDatePlayed() {
        return datePlayed;
    }

    /**
     * Sets the date the match was played.
     *
     * @param datePlayed the new date the match was played.
     */
    public void setDatePlayed(LocalDateTime datePlayed) {
        this.datePlayed = datePlayed;
    }
}
