package com.g1.mychess.user.dto;

import java.time.LocalDate;

public class TournamentPlayerDTO {

    private Long playerId;
    private String profileName;
    private LocalDate joinedDate;
    private Integer tournamentCount;
    private LocalDate lastActive;
    private int glickoRating;
    private double ratingDeviation;
    private double volatility;

    // Constructor
    public TournamentPlayerDTO(Long playerId, String profileName, LocalDate joinedDate, Integer tournamentCount,
                               LocalDate lastActive, int glickoRating, double ratingDeviation, double volatility) {
        this.playerId = playerId;
        this.profileName = profileName;
        this.joinedDate = joinedDate;
        this.tournamentCount = tournamentCount;
        this.lastActive = lastActive;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
    }

    // Getters and Setters

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Integer getTournamentCount() {
        return tournamentCount;
    }

    public void setTournamentCount(Integer tournamentCount) {
        this.tournamentCount = tournamentCount;
    }

    public LocalDate getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDate lastActive) {
        this.lastActive = lastActive;
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
}

