package com.g1.mychess.tournament.dto;

/**
 * Data Transfer Object representing a player in the chess tournament system.
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
     * Constructs a new {@code PlayerDTO} with the specified details.
     *
     * @param id              the ID of the player
     * @param isBlacklisted   whether the player is blacklisted
     * @param username        the username of the player
     * @param email           the email address of the player
     * @param age             the age of the player
     * @param gender          the gender of the player
     * @param glickoRating    the Glicko rating of the player
     * @param ratingDeviation the rating deviation of the player
     * @param volatility      the volatility of the player's rating
     */
    public PlayerDTO(Long id, boolean isBlacklisted, String username, String email, int age, String gender,
                     double glickoRating, double ratingDeviation, double volatility) {
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

    /**
     * Returns the player's ID.
     *
     * @return the player's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the player's ID.
     *
     * @param id the player's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Indicates whether the player is blacklisted.
     *
     * @return {@code true} if the player is blacklisted, otherwise {@code false}
     */
    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    /**
     * Sets the blacklisted status of the player.
     *
     * @param blacklisted {@code true} if the player is blacklisted, otherwise {@code false}
     */
    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    /**
     * Returns the player's username.
     *
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     *
     * @param username the player's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the player's email address.
     *
     * @return the player's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the player's email address.
     *
     * @param email the player's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the player's age.
     *
     * @return the player's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the player's age.
     *
     * @param age the player's age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Returns the player's gender.
     *
     * @return the player's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the player's gender.
     *
     * @param gender the player's gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the player's Glicko rating.
     *
     * @return the player's Glicko rating
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the player's Glicko rating.
     *
     * @param glickoRating the player's Glicko rating
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Returns the player's rating deviation.
     *
     * @return the player's rating deviation
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the player's rating deviation.
     *
     * @param ratingDeviation the player's rating deviation
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Returns the volatility of the player's rating.
     *
     * @return the player's rating volatility
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the volatility of the player's rating.
     *
     * @param volatility the player's rating volatility
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }
}
