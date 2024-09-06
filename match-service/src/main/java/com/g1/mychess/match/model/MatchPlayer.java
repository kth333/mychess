package com.g1.mychess.match.model;

import jakarta.persistence.*;

@Entity
@Table(name = "match_player")
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "initial_rating", nullable = false)
    private int initialRating;

    @Column(name = "new_rating", nullable = false)
    private int newRating;

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

    public int getInitialRating() { return initialRating; }

    public void setInitialRating(int initialRating) { this.initialRating = initialRating; }

    public int getNewRating() { return newRating; }

    public void setNewRating(int newRating) { this.newRating = newRating; }

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
