package com.g1.mychess.match.model;

import jakarta.persistence.*;

@Entity
@Table(name = "match_players")
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "current_round", nullable = false)
    private int currentRound;

    @Column(name = "points")
    private double points;

    @Column(name = "initial_rating", nullable = false)
    private double initialRating;

    @Column(name = "initial_rating_deviation", nullable = false)
    private double initialRatingDeviation;

    @Column(name = "initial_volatility", nullable = false)
    private double initialVolatility;

    @Column(name = "new_rating")
    private double newRating;

    @Column(name = "new_rating_deviation")
    private double newRatingDeviation;

    @Column(name = "new_volatility")
    private double newVolatility;

    @Column(name = "predicted_win_rate")
    private Double predictedWinRate;

    @Column(name = "performance_rating")
    private Integer performanceRating;

    @Column(name = "game_duration")
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

    public Long getPlayerId() { return playerId; }

    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public int getCurrentRound() { return currentRound; }

    public void setCurrentRound(int currentRound) { this.currentRound = currentRound; }

    public double getPoints() { return points; }

    public void setPoints(double points) { this.points = points; }

    public double getInitialRating() { return initialRating; }

    public void setInitialRating(double initialRating) { this.initialRating = initialRating; }

    public double getInitialRatingDeviation() { return initialRatingDeviation; }

    public void setInitialRatingDeviation(double initialRatingDeviation) { this.initialRatingDeviation = initialRatingDeviation; }

    public double getInitialVolatility() { return initialVolatility; }

    public void setInitialVolatility(double initialVolatility) { this.initialVolatility = initialVolatility; }

    public double getNewRating() { return newRating; }

    public void setNewRating(double newRating) { this.newRating = newRating; }

    public double getNewRatingDeviation() { return newRatingDeviation; }

    public void setNewRatingDeviation(double newRatingDeviation) {
        this.newRatingDeviation = newRatingDeviation;
    }

    public double getNewVolatility() { return newVolatility; }

    public void setNewVolatility(double newVolatility) { this.newVolatility = newVolatility; }

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
