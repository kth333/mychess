package com.g1.mychess.match.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entity representing a match player in a chess tournament.
 *
 * The {@code MatchPlayer} class stores details about a player in a particular match, including ratings, points, round number,
 * and game results. This entity is mapped to the `match_players` table in the database.
 *
 * Annotations ensure the correctness of data, such as non-null constraints and value limits on specific fields.
 */
@Entity
@Table(name = "match_players")
public class MatchPlayer {

    /**
     * Unique identifier for the match player record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The associated {@link Match} object that this player participated in.
     */
    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    @NotNull
    private Match match;

    /**
     * The ID of the player participating in the match.
     */
    @Column(name = "player_id", nullable = false)
    @NotNull
    private Long playerId;

    /**
     * The ID of the opponent player in this match.
     */
    @Column(name = "opponent_id", nullable = false)
    @NotNull
    private Long opponentId;

    /**
     * The current round number within the tournament.
     */
    @Column(name = "current_round", nullable = false)
    @NotNull
    @Min(1)
    private int currentRound;

    /**
     * The number of points scored by the player in the match.
     */
    @Column(name = "points")
    @Min(0)
    private double points;

    /**
     * The Glicko rating of the player before the match.
     */
    @Column(name = "glicko_rating", nullable = false)
    @NotNull
    private double glickoRating;

    /**
     * The rating deviation associated with the player's Glicko rating.
     */
    @Column(name = "rating_deviation", nullable = false)
    @NotNull
    private double ratingDeviation;

    /**
     * The player's volatility factor in the Glicko rating system.
     */
    @Column(name = "volatility", nullable = false)
    @NotNull
    private double volatility;

    /**
     * Predicted win rate of the player, expressed as a decimal between 0 and 1.
     */
    @Column(name = "predicted_win_rate")
    @Min(0)
    @Max(1)
    private Double predictedWinRate;

    /**
     * The performance rating of the player based on this match's outcome.
     */
    @Column(name = "performance_rating")
    @Min(0)
    private Integer performanceRating;

    /**
     * The duration of the game in seconds.
     */
    @Column(name = "game_duration")
    @Min(0)
    private Long gameDuration;

    /**
     * The result of the match for this player, as a value from the {@link Result} enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private Result result;

    /**
     * Enum representing the possible outcomes of a match.
     */
    public enum Result {
        WIN,
        LOSS,
        DRAW
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the match player.
     * @return the unique ID of the match player.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the match player.
     * @param id the unique ID to set for the match player.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the associated match object in which the player participated.
     * @return the match object.
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Sets the associated match object for the player.
     * @param match the match to associate with the player.
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * Gets the player's unique ID in the match.
     * @return the player ID.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player's unique ID in the match.
     * @param playerId the player ID to set.
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the opponent's unique ID in the match.
     * @return the opponent ID.
     */
    public Long getOpponentId() {
        return opponentId;
    }

    /**
     * Sets the opponent's unique ID in the match.
     * @param opponentId the opponent ID to set.
     */
    public void setOpponentId(Long opponentId) {
        this.opponentId = opponentId;
    }

    /**
     * Gets the current round number in the tournament.
     * @return the current round number.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Sets the current round number in the tournament.
     * @param currentRound the current round number to set.
     */
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * Gets the number of points scored by the player in the match.
     * @return the player's points.
     */
    public double getPoints() {
        return points;
    }

    /**
     * Sets the number of points scored by the player in the match.
     * @param points the points to set for the player.
     */
    public void setPoints(double points) {
        this.points = points;
    }

    /**
     * Gets the Glicko rating of the player.
     * @return the Glicko rating.
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player.
     * @param glickoRating the Glicko rating to set.
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the rating deviation for the player's Glicko rating.
     * @return the rating deviation.
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the rating deviation for the player's Glicko rating.
     * @param ratingDeviation the rating deviation to set.
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the player's volatility factor in the Glicko rating system.
     * @return the volatility factor.
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the player's volatility factor in the Glicko rating system.
     * @param volatility the volatility factor to set.
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    /**
     * Gets the predicted win rate of the player, as a decimal between 0 and 1.
     * @return the predicted win rate.
     */
    public Double getPredictedWinRate() {
        return predictedWinRate;
    }

    /**
     * Sets the predicted win rate of the player, as a decimal between 0 and 1.
     * @param predictedWinRate the predicted win rate to set.
     */
    public void setPredictedWinRate(Double predictedWinRate) {
        this.predictedWinRate = predictedWinRate;
    }

    /**
     * Gets the result of the match for this player.
     * @return the match result.
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the result of the match for this player.
     * @param result the match result to set.
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Gets the performance rating of the player for this match.
     * @return the performance rating.
     */
    public Integer getPerformanceRating() {
        return performanceRating;
    }

    /**
     * Sets the performance rating of the player for this match.
     * @param performanceRating the performance rating to set.
     */
    public void setPerformanceRating(Integer performanceRating) {
        this.performanceRating = performanceRating;
    }

    /**
     * Gets the duration of the game in seconds.
     * @return the game duration.
     */
    public Long getGameDuration() {
        return gameDuration;
    }

    /**
     * Sets the duration of the game in seconds.
     * @param gameDuration the game duration to set.
     */
    public void setGameDuration(Long gameDuration) {
        this.gameDuration = gameDuration;
    }
}
