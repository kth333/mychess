package com.g1.mychess.player.dto;

import com.g1.mychess.player.model.CustomChessRank;

public class LeaderboardProfileDTO {

    private Long playerId;
    private String username;
    private CustomChessRank rank;
    private String country;
    private double glickoRating;

    // Constructor
    public LeaderboardProfileDTO(Long playerId, String username, CustomChessRank rank, String country, double glickoRating) {
        this.playerId = playerId;
        this.username = username;
        this.rank = rank;
        this.country = country;
        this.glickoRating = glickoRating;
    }

    // Getters and Setters
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CustomChessRank getRank() {
        return rank;
    }

    public void setRank(CustomChessRank rank) {
        this.rank = rank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }
}
