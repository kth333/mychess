package com.g1.mychess.match.dto;

/**
 * Data Transfer Object (DTO) for updating a player's rating information.
 * This class contains the player's Glicko rating, rating deviation, and volatility values.
 */
public class PlayerRatingUpdateDTO {

    /**
     * The unique identifier of the player whose rating is being updated.
     */
    private Long playerId;

    /**
     * The Glicko rating of the player, which represents their skill level in the game.
     */
    private double glickoRating;

    /**
     * The rating deviation for the player, which reflects the uncertainty of the player's rating.
     */
    private double ratingDeviation;

    /**
     * The volatility of the player's rating, which indicates how stable the player's rating is.
     */
    private double volatility;

    // Constructors

    /**
     * Constructor for PlayerRatingUpdateDTO with specified player ID, Glicko rating, rating deviation, and volatility.
     *
     * @param playerId The unique identifier for the player.
     * @param glickoRating The Glicko rating of the player.
     * @param ratingDeviation The rating deviation for the player.
     * @param volatility The volatility of the player's rating.
     */
    public PlayerRatingUpdateDTO(Long playerId, double glickoRating, double ratingDeviation, double volatility) {
        this.playerId = playerId;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier of the player.
     *
     * @return The player's ID.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the unique identifier of the player.
     *
     * @param playerId The player ID to set.
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the Glicko rating of the player.
     *
     * @return The Glicko rating.
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player.
     *
     * @param glickoRating The Glicko rating to set.
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the rating deviation of the player.
     *
     * @return The rating deviation.
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the rating deviation of the player.
     *
     * @param ratingDeviation The rating deviation to set.
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the volatility of the player's rating.
     *
     * @return The volatility value.
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the volatility of the player's rating.
     *
     * @param volatility The volatility value to set.
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }
}
