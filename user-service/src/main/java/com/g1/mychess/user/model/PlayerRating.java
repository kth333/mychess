package com.g1.mychess.user.model;

import jakarta.persistence.*;

@Entity
@Table(name = "player_ratings")
public class PlayerRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "glicko_rating", nullable = false)
    private int glickoRating;

    @Column(name = "rating_deviation", nullable = false)
    private double ratingDeviation;

    @Column(name = "volatility", nullable = false)
    private double volatility;

    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

    public int getGlickoRating() { return glickoRating; }

    public void setGlickoRating(int glickoRating) { this.glickoRating = glickoRating; }

    public double getRatingDeviation() { return ratingDeviation; }

    public void setRatingDeviation(double ratingDeviation) { this.ratingDeviation = ratingDeviation; }

    public double getVolatility() { return volatility; }

    public void setVolatility(double volatility) { this.volatility = volatility; }
}

