package com.g1.mychess.tournament.dto;

public class PlayerDTO {

    private Long id;
    private boolean isBlacklisted;
    private String username;
    private int age;
    private String gender;

    // Rating details
    private int glickoRating;
    private double ratingDeviation;
    private double volatility;

    // Full Constructor
    public PlayerDTO(Long id, boolean isBlacklisted, String username, int age, String gender, int glickoRating, double ratingDeviation, double volatility) {
        this.id = id;
        this.isBlacklisted = isBlacklisted;
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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