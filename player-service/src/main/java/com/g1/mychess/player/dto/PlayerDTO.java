package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) representing a player's basic details and rating information.
 * This class encapsulates information about a player's ID, blacklist status, username,
 * email, age, gender, and chess rating details (Glicko rating, rating deviation, and volatility).
 */
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

    /**
     * Constructs a PlayerDTO with all necessary player details including rating information.
     *
     * @param id the unique identifier for the player
     * @param isBlacklisted the blacklist status of the player
     * @param username the username of the player
     * @param email the email of the player
     * @param age the age of the player
     * @param gender the gender of the player
     * @param glickoRating the Glicko rating of the player
     * @param ratingDeviation the Glicko rating deviation of the player
     * @param volatility the volatility of the player's Glicko rating
     */
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

    /**
     * Gets the player's unique identifier.
     *
     * @return the player's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param id the player's ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Checks if the player is blacklisted.
     *
     * @return true if the player is blacklisted, false otherwise
     */
    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    /**
     * Sets the blacklisted status of the player.
     *
     * @param blacklisted true to mark the player as blacklisted, false otherwise
     */
    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    /**
     * Gets the player's username.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the player's email.
     *
     * @return the email of the player
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the player's email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the player's age.
     *
     * @return the age of the player
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the player's age.
     *
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the player's gender.
     *
     * @return the gender of the player
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the player's gender.
     *
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the player's Glicko rating.
     *
     * @return the Glicko rating of the player
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the player's Glicko rating.
     *
     * @param glickoRating the Glicko rating to set
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the player's Glicko rating deviation.
     *
     * @return the rating deviation of the player
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the player's Glicko rating deviation.
     *
     * @param ratingDeviation the rating deviation to set
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the player's Glicko volatility.
     *
     * @return the volatility of the player's Glicko rating
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the player's Glicko volatility.
     *
     * @param volatility the volatility to set
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }
}
