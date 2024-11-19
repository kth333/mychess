package com.g1.mychess.tournament.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a player in a tournament.
 */
public class TournamentPlayerDTO {

    private Long tournamentId;
    private Long playerId;
    private LocalDateTime signUpDateTime;
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;
    private double points;
    private int roundsPlayed;
    private String status;

    /**
     * Default constructor.
     */
    public TournamentPlayerDTO() {}

    /**
     * Constructs a new {@code TournamentPlayerDTO} with the specified details.
     *
     * @param tournamentId the ID of the tournament
     * @param playerId     the ID of the player
     * @param signUpDateTime the date and time when the player signed up
     * @param glickoRating the player's Glicko rating
     * @param ratingDeviation the player's rating deviation
     * @param volatility the player's rating volatility
     * @param points the points earned by the player
     * @param roundsPlayed the number of rounds played by the player
     * @param status the current status of the player in the tournament
     */
    public TournamentPlayerDTO(Long tournamentId, Long playerId, LocalDateTime signUpDateTime, double glickoRating, double ratingDeviation, double volatility, double points, int roundsPlayed, String status) {
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
     * Returns the ID of the tournament.
     *
     * @return the ID of the tournament
     */
    public Long getTournamentId() { return tournamentId; }

    /**
     * Sets the ID of the tournament.
     *
     * @param tournamentId the ID of the tournament
     */
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Returns the ID of the player.
     *
     * @return the ID of the player
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the ID of the player.
     *
     * @param playerId the ID of the player
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Returns the date and time when the player signed up.
     *
     * @return the sign-up date and time
     */
    public LocalDateTime getSignUpDateTime() {
        return signUpDateTime;
    }

    /**
     * Sets the date and time when the player signed up.
     *
     * @param signUpDateTime the sign-up date and time
     */
    public void setSignUpDateTime(LocalDateTime signUpDateTime) {
        this.signUpDateTime = signUpDateTime;
    }

    /**
     * Returns the Glicko rating of the player.
     *
     * @return the Glicko rating of the player
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player.
     *
     * @param glickoRating the Glicko rating of the player
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Returns the rating deviation of the player.
     *
     * @return the rating deviation of the player
     */
    public double getRatingDeviation() { 
        return ratingDeviation; 
    }

    /**
     * Sets the rating deviation of the player.
     *
     * @param ratingDeviation the rating deviation of the player
     */
    public void setRatingDeviation(double ratingDeviation) { 
        this.ratingDeviation = ratingDeviation; 
    }

    /**
     * Returns the volatility of the player's rating.
     *
     * @return the volatility of the player's rating
     */
    public double getVolatility() { 
        return volatility; 
    }

    /**
     * Sets the volatility of the player's rating.
     *
     * @param volatility the volatility of the player's rating
     */
    public void setVolatility(double volatility) { 
        this.volatility = volatility; 
    }

    /**
     * Returns the points earned by the player in the tournament.
     *
     * @return the points earned by the player
     */
    public double getPoints() {
        return points;
    }

    /**
     * Sets the points earned by the player in the tournament.
     *
     * @param points the points earned by the player
     */
    public void setPoints(double points) {
        this.points = points;
    }

    /**
     * Returns the number of rounds the player has played.
     *
     * @return the number of rounds the player has played
     */
    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    /**
     * Sets the number of rounds the player has played.
     *
     * @param roundsPlayed the number of rounds the player has played
     */
    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    /**
     * Returns the current status of the player in the tournament.
     *
     * @return the current status of the player
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the player in the tournament.
     *
     * @param status the current status of the player
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
