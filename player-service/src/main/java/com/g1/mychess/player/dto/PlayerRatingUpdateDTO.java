package com.g1.mychess.player.dto;

public class PlayerRatingUpdateDTO {
    private Long playerId;
    private int glickoRating;
    private double ratingDeviation;
    private double volatility;
    private Long tournamentId;
    private String result; // WIN, LOSS, or DRAW

    // Constructors, Getters, and Setters
    public PlayerRatingUpdateDTO(Long playerId, int glickoRating, double ratingDeviation, double volatility, Long tournamentId, String result) {
        this.playerId = playerId;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.tournamentId = tournamentId;
        this.result = result;
    }

    // Getters and Setters


    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public int getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(int glickoRating) {
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

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}