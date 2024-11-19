package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) class for updating a player's rating information.
 * This class contains the necessary fields for updating a player's Glicko rating, rating deviation, and volatility.
 */
public class PlayerRatingUpdateDTO {

    private Long playerId;
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;

    /**
     * Constructs a PlayerRatingUpdateDTO with the provided parameters.
     *
     * @param playerId the ID of the player
     * @param glickoRating the Glicko rating of the player
     * @param ratingDeviation the rating deviation of the player
     * @param volatility the volatility of the player's rating
     */
    public PlayerRatingUpdateDTO(Long playerId, double glickoRating, double ratingDeviation, double volatility) {
        this.playerId = playerId;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
    }

    /**
     * Gets the player ID.
     *
     * @return the player ID
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player ID.
     *
     * @param playerId the player ID
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the Glicko rating of the player.
     *
     * @return the Glicko rating
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player.
     *
     * @param glickoRating the Glicko rating
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the rating deviation of the player.
     *
     * @return the rating deviation
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the rating deviation of the player.
     *
     * @param ratingDeviation the rating deviation
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the volatility of the player's rating.
     *
     * @return the volatility
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the volatility of the player's rating.
     *
     * @param volatility the volatility
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }
}
