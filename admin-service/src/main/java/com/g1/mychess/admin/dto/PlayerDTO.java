package com.g1.mychess.admin.dto;

public class PlayerDTO {

    private Long id;
    private boolean isBlacklisted;
    private String username;
    private String email;
    private int age;
    private String gender;

    // Rating details
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;

    // Full Constructor
    public PlayerDTO(Long id, boolean isBlacklisted, String username, String email, int age, String gender, double glickoRating, double ratingDeviation, double volatility) {
        this.id = id;
        this.isBlacklisted = isBlacklisted;
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
