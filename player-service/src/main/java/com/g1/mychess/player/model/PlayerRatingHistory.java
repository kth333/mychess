package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "player_rating_history")
public class PlayerRatingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne  // A player can have many historical ratings
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "glicko_rating", nullable = false)
    private double glickoRating;

    @Column(name = "rating_deviation", nullable = false)
    private double ratingDeviation;

    @Column(name = "volatility", nullable = false)
    private double volatility;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDateTime date;  // Date when this rating was recorded

    public PlayerRatingHistory(){
    }

    public PlayerRatingHistory(Player player, double glickoRating, double ratingDeviation, double volatility, LocalDateTime now) {
        this.player = player;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.date = now;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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