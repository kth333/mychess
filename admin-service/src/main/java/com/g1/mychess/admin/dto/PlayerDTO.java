package com.g1.mychess.admin.dto;

/**
 * Data Transfer Object (DTO) representing a player in the chess system. It contains information
 * about the player's personal details, blacklisting status, and Glicko rating system data.
 */
public class PlayerDTO {

    private Long id;               // Unique identifier for the player
    private boolean isBlacklisted; // Indicates if the player is blacklisted
    private String username;       // Username of the player
    private String email;          // Email address of the player
    private int age;               // Age of the player
    private String gender;         // Gender of the player

    // Rating details for the player based on the Glicko rating system
    private double glickoRating;   // Glicko rating of the player
    private double ratingDeviation; // Rating deviation of the player
    private double volatility;      // Volatility of the player's rating

    // Full Constructor to initialize PlayerDTO object with all properties
    /**
     * Constructor to initialize the PlayerDTO with the player's details and rating information.
     *
     * @param id the unique ID of the player
     * @param isBlacklisted indicates if the player is blacklisted
     * @param username the username of the player
     * @param email the email address of the player
     * @param age the age of the player
     * @param gender the gender of the player
     * @param glickoRating the Glicko rating of the player
     * @param ratingDeviation the rating deviation of the player
     * @param volatility the volatility of the player's rating
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
     * Gets the unique ID of the player.
     *
     * @return the ID of the player
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the player.
     *
     * @param id the ID to set for the player
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the blacklisted status of the player.
     *
     * @return true if the player is blacklisted, otherwise false
     */
    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    /**
     * Sets the blacklisted status of the player.
     *
     * @param blacklisted true if the player should be blacklisted, otherwise false
     */
    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    /**
     * Gets the username of the player.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the player.
     *
     * @param username the username to set for the player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the player.
     *
     * @return the email address of the player
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the player.
     *
     * @param email the email address to set for the player
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the age of the player.
     *
     * @return the age of the player
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the player.
     *
     * @param age the age to set for the player
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the gender of the player.
     *
     * @return the gender of the player
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the player.
     *
     * @param gender the gender to set for the player
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the Glicko rating of the player.
     *
     * @return the Glicko rating of the player
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player.
     *
     * @param glickoRating the Glicko rating to set for the player
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the rating deviation of the player.
     *
     * @return the rating deviation of the player
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the rating deviation of the player.
     *
     * @param ratingDeviation the rating deviation to set for the player
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the volatility of the player's rating.
     *
     * @return the volatility of the player's rating
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the volatility of the player's rating.
     *
     * @param volatility the volatility to set for the player
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }
}