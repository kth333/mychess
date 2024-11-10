package com.g1.mychess.player.dto;

import java.time.LocalDateTime;

public class PlayerRatingHistoryDTO {
    private Long id;
    private Long playerId;
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;
    private LocalDateTime date;

    public PlayerRatingHistoryDTO() {
    }

    public PlayerRatingHistoryDTO(Long id, Long playerId, double glickoRating, double ratingDeviation, double volatility, LocalDateTime date) {
        this.id = id;
        this.playerId = playerId;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.date = date;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
