package com.g1.mychess.match.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a player in a tournament.
 * This class is used to carry information about a player participating in a specific tournament.
 */
public class TournamentPlayerDTO {

    /**
     * The ID of the tournament the player is participating in.
     */
    private Long tournamentId;

    /**
     * The ID of the player in the system.
     */
    private Long playerId;

    /**
     * The date and time when the player signed up for the tournament.
     */
    private LocalDateTime signUpDateTime;

    /**
     * The Glicko rating of the player, which is used to measure their skill level.
     */
    private double glickoRating;

    /**
     * The rating deviation for the player's Glicko rating, representing the uncertainty of the rating.
     */
    private double ratingDeviation;

    /**
     * The volatility of the player's Glicko rating, indicating how consistent or unpredictable their performance is.
     */
    private double volatility;

    /**
     * The points accumulated by the player in the tournament.
     */
    private double points;

    /**
     * The number of rounds the player has participated in the tournament.
     */
    private int roundsPlayed;

    /**
     * The current status of the player in the tournament (e.g., "active", "eliminated").
     */
    private String status;

    // Constructors, getters, setters

    /**
     * Default constructor for TournamentPlayerDTO.
     */
    public TournamentPlayerDTO() {}

    /**
     * Constructor to initialize all fields of the TournamentPlayerDTO.
     *
     * @param tournamentId The ID of the tournament.
     * @param playerId The ID of the player.
     * @param signUpDateTime The date and time the player signed up.
     * @param glickoRating The player's Glicko rating.
     * @param ratingDeviation The player's Glicko rating deviation.
     * @param volatility The player's Glicko rating volatility.
     * @param points The points the player has accumulated.
     * @param roundsPlayed The number of rounds the player has played.
     * @param status The status of the player in the tournament.
     */
    public TournamentPlayerDTO(Long tournamentId, Long playerId, LocalDateTime signUpDateTime, double glickoRating,
                               double ratingDeviation, double volatility, double points, int roundsPlayed, String status) {
        this.tournamentId = tournamentId;
        this.playerId = playerId;
        this.signUpDateTime = signUpDateTime;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.points = points;
        this.roundsPlayed = roundsPlayed;
        this.status = status;
    }

    /**
     * Gets the tournament ID.
     *
     * @return The tournament ID.
     */
    public Long getTournamentId() { return tournamentId; }

    /**
     * Sets the tournament ID.
     *
     * @param tournamentId The tournament ID to set.
     */
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Gets the player ID.
     *
     * @return The player ID.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player ID.
     *
     * @param playerId The player ID to set.
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the sign-up date and time of the player.
     *
     * @return The sign-up date and time.
     */
    public LocalDateTime getSignUpDateTime() {
        return signUpDateTime;
    }

    /**
     * Sets the sign-up date and time for the player.
     *
     * @param signUpDateTime The sign-up date and time to set.
     */
    public void setSignUpDateTime(LocalDateTime signUpDateTime) {
        this.signUpDateTime = signUpDateTime;
    }

    /**
     * Gets the Glicko rating of the player.
     *
     * @return The Glicko rating.
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player.
     *
     * @param glickoRating The Glicko rating to set.
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the rating deviation of the player.
     *
     * @return The rating deviation.
     */
    public double getRatingDeviation() { return ratingDeviation; }

    /**
     * Sets the rating deviation of the player.
     *
     * @param ratingDeviation The rating deviation to set.
     */
    public void setRatingDeviation(double ratingDeviation) { this.ratingDeviation = ratingDeviation; }

    /**
     * Gets the volatility of the player's rating.
     *
     * @return The volatility.
     */
    public double getVolatility() { return volatility; }

    /**
     * Sets the volatility of the player's rating.
     *
     * @param volatility The volatility to set.
     */
    public void setVolatility(double volatility) { this.volatility = volatility; }

    /**
     * Gets the points accumulated by the player.
     *
     * @return The points.
     */
    public double getPoints() {
        return points;
    }

    /**
     * Sets the points accumulated by the player.
     *
     * @param points The points to set.
     */
    public void setPoints(double points) {
        this.points = points;
    }

    /**
     * Gets the number of rounds the player has played in the tournament.
     *
     * @return The number of rounds played.
     */
    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    /**
     * Sets the number of rounds the player has played in the tournament.
     *
     * @param roundsPlayed The number of rounds played to set.
     */
    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    /**
     * Gets the status of the player in the tournament.
     *
     * @return The status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the player in the tournament.
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
