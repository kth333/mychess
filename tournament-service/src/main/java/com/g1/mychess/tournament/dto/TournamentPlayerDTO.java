package com.g1.mychess.tournament.dto;

import java.time.LocalDateTime;

public class TournamentPlayerDTO {

    private Long id;
    private Long tournamentId;
    private Long playerId;
    private LocalDateTime signUpDateTime;
    private int glickoRating;
    private double points;
    private int roundsPlayed;
    private String status;

    // Constructors
    public TournamentPlayerDTO() {}

    public TournamentPlayerDTO(Long id, Long tournamentId, Long playerId, LocalDateTime signUpDateTime, int glickoRating,
                               double points, int roundsPlayed, String status) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.playerId = playerId;
        this.signUpDateTime = signUpDateTime;
        this.glickoRating = glickoRating;
        this.points = points;
        this.roundsPlayed = roundsPlayed;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

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
