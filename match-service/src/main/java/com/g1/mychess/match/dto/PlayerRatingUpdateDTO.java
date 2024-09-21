package com.g1.mychess.match.dto;

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
}
