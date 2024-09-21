package com.g1.mychess.match.dto;

import java.time.LocalDateTime;

public class TournamentPlayerDTO {

    private Long id;
    private Long playerId;
    private LocalDateTime signUpDateTime;
    private int glickoRating;
    private double points;
    private int roundsPlayed;
    private String status; // Use enum if needed

    // Constructors, getters, setters

    public TournamentPlayerDTO() {}

    public TournamentPlayerDTO(Long id, Long playerId, LocalDateTime signUpDateTime, int glickoRating, double points, int roundsPlayed, String status) {
        this.id = id;
        this.playerId = playerId;
        this.signUpDateTime = signUpDateTime;
        this.glickoRating = glickoRating;
        this.points = points;
        this.roundsPlayed = roundsPlayed;
        this.status = status;
    }

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

    public LocalDateTime getSignUpDateTime() {
        return signUpDateTime;
    }

    public void setSignUpDateTime(LocalDateTime signUpDateTime) {
        this.signUpDateTime = signUpDateTime;
    }

    public int getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(int glickoRating) {
        this.glickoRating = glickoRating;
    }

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
