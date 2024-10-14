package com.g1.mychess.match.dto;

public class PlayerRatingUpdateDTO {
    private Long playerId;
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;

    // Constructors, Getters, and Setters
    public PlayerRatingUpdateDTO(Long playerId, double glickoRating, double ratingDeviation, double volatility) {
        this.playerId = playerId;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
    }

    // Getters and Setters
    public Long getPlayerId() { return playerId; }

    public void setPlayedId(Long playerId) { this.playerId = playerId; }

    public double getGlickoRating() { return glickoRating; }

    public void setGlickoRating(double glickoRating) { this.glickoRating = glickoRating; }

    public double getRatingDeviation() { return ratingDeviation; }

    public void setRatingDeviation(double ratingDeviation) { this.ratingDeviation = ratingDeviation; }

    public double getVolatility() { return volatility; }

    public void setVolatility(double volatility) { this.volatility = volatility; }
}
