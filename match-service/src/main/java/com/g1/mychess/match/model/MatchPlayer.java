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

    @Column(name = "opponent_id", nullable = false)
    private Long opponentId;

    @Column(name = "current_round", nullable = false)
    private int currentRound;

    @Column(name = "points")
    private double points;

    @Column(name = "glicko_rating", nullable = false)
    private double glickoRating;

    @Column(name = "rating_deviation", nullable = false)
    private double ratingDeviation;

    @Column(name = "volatility", nullable = false)
    private double volatility;

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
