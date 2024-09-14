package com.g1.mychess.player.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_rating_history")
public class PlayerRatingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // A player can have many historical ratings
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "glicko_rating", nullable = false)
    private int glickoRating;

    @Column(name = "rating_deviation", nullable = false)
    private double ratingDeviation;

    @Column(name = "volatility", nullable = false)
    private double volatility;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;  // Date when this rating was recorded

    public PlayerRatingHistory() {}

    // Getters and Setters

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}