package com.g1.mychess.match.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "match_players")
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    @NotNull  // Ensure the match cannot be null
    private Match match;

    @Column(name = "player_id", nullable = false)
    @NotNull  // Ensure player ID cannot be null
    private Long playerId;

    @Column(name = "opponent_id", nullable = false)
    @NotNull  // Ensure opponent ID cannot be null
    private Long opponentId;

    @Column(name = "current_round", nullable = false)
    @NotNull  // Ensure current round cannot be null
    @Min(1)   // Ensure the current round is at least 1
    private int currentRound;

    @Column(name = "points")
    @Min(0)   // Ensure points cannot be negative
    private double points;

    @Column(name = "glicko_rating", nullable = false)
    @NotNull  // Ensure Glicko rating cannot be null
    private double glickoRating;

    @Column(name = "rating_deviation", nullable = false)
    @NotNull  // Ensure rating deviation cannot be null
    private double ratingDeviation;

    @Column(name = "volatility", nullable = false)
    @NotNull  // Ensure volatility cannot be null
    private double volatility;

    @Column(name = "predicted_win_rate")
    @Min(0)   // Ensure predicted win rate is not negative
    @Max(1)   // Ensure predicted win rate is not more than 1 (0% to 100%)
    private Double predictedWinRate;

    @Column(name = "performance_rating")
    @Min(0)   // Ensure performance rating is not negative
    private Integer performanceRating;

    @Column(name = "game_duration")
    @Min(0)   // Ensure game duration is not negative
    private Long gameDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private Result result;

    public enum Result {
        WIN,
        LOSS,
        DRAW
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(Long opponentId) {
        this.opponentId = opponentId;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    public double getRatingDeviation() {
        return ratingDeviation;
    }

    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public Double getPredictedWinRate() {
        return predictedWinRate;
    }

    public void setPredictedWinRate(Double predictedWinRate) {
        this.predictedWinRate = predictedWinRate;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Integer getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(Integer performanceRating) {
        this.performanceRating = performanceRating;
    }

    public Long getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(Long gameDuration) {
        this.gameDuration = gameDuration;
    }
}
