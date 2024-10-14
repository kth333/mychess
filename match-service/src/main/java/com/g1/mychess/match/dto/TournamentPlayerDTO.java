package com.g1.mychess.match.dto;

import java.time.LocalDateTime;

public class TournamentPlayerDTO {

    private Long tournamentId;
    private Long playerId;
    private LocalDateTime signUpDateTime;
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;
    private double points;
    private int roundsPlayed;
    private String status;

    // Constructors, getters, setters

    public TournamentPlayerDTO() {}

    public TournamentPlayerDTO(Long tournamentId, Long playerId, LocalDateTime signUpDateTime, double glickoRating, double ratingDeviation, double volatility, double points, int roundsPlayed, String status) {
        this.tournamentId = tournamentId;
        this.playerId = playerId;
        this.signUpDateTime = signUpDateTime;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.points = points;
        this.roundsPlayed = roundsPlayed;
        this.status = status;
    }

    public Long getTournamentId() { return tournamentId; }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public LocalDateTime getSignUpDateTime() {
        return signUpDateTime;
    }

    public void setSignUpDateTime(LocalDateTime signUpDateTime) {
        this.signUpDateTime = signUpDateTime;
    }

    public double getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    public double getRatingDeviation() { return ratingDeviation; }

    public void setRatingDeviation(double ratingDeviation) { this.ratingDeviation = ratingDeviation; }

    public double getVolatility() { return volatility; }

    public void setVolatility(double volatility) { this.volatility = volatility; }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
