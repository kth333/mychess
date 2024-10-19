package com.g1.mychess.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournament_player")
public class TournamentPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    @NotNull
    private Tournament tournament;

    @Column(name = "player_id", nullable = false)
    @NotNull
    private Long playerId;

    @Column(name = "sign_up_date_time", nullable = false)
    @NotNull
    private LocalDateTime signUpDateTime;

    @Column(name = "glicko_rating", nullable = false)
    @Min(0)
    private double glickoRating;

    @Column(name = "rating_deviation", nullable = false)
    @Min(0)
    private double ratingDeviation;

    @Column(name = "volatility", nullable = false)
    @Min(0)
    private double volatility;

    @Column(name = "points")
    @Min(0)
    private double points = 0.0;

    @Column(name = "rounds_played")
    @Min(0)
    private int roundsPlayed = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private TournamentPlayerStatus status;

    public enum TournamentPlayerStatus {
        ACTIVE,         // Currently participating
        ELIMINATED,     // Eliminated from the tournament
        WINNER,         // Winner of the tournament
        RUNNER_UP,      // Finished in second place
        THIRD_PLACE,    // Finished in third place
        DISQUALIFIED,   // Disqualified from the tournament
        WITHDRAWN,      // Voluntarily withdrew or retired from the tournament
        FINALIST        // Reached the final round
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
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

    public double getPoints() { return points; }

    public void setPoints(double points) { this.points = points; }

    public int getRoundsPlayed() { return roundsPlayed; }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public TournamentPlayerStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentPlayerStatus status) {
        this.status = status;
    }
}
