package com.g1.mychess.player.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) class that holds historical data for a player's rating.
 * This class contains the rating information, such as the Glicko rating, rating deviation, volatility,
 * and the timestamp when the rating was recorded.
 */
public class PlayerRatingHistoryDTO {

    private Long id;
    private Long playerId;
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;
    private LocalDateTime date;

    /**
     * Default constructor for PlayerRatingHistoryDTO.
     */
    public PlayerRatingHistoryDTO() {
    }

    /**
     * Constructs a PlayerRatingHistoryDTO with the provided parameters.
     *
     * @param id the ID of the rating history entry
     * @param playerId the ID of the player whose rating history this entry pertains to
     * @param glickoRating the Glicko rating of the player at the time of this entry
     * @param ratingDeviation the rating deviation at the time of this entry
     * @param volatility the volatility of the player's rating at the time of this entry
     * @param date the date and time when the rating history was recorded
     */
    public PlayerRatingHistoryDTO(Long id, Long playerId, double glickoRating, double ratingDeviation, double volatility, LocalDateTime date) {
        this.id = id;
        this.playerId = playerId;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.date = date;
    }

    /**
     * Gets the ID of the rating history entry.
     *
     * @return the ID of the rating history entry
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the rating history entry.
     *
     * @param id the ID of the rating history entry
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the player associated with this rating history entry.
     *
     * @return the ID of the player
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the ID of the player associated with this rating history entry.
     *
     * @param playerId the ID of the player
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the Glicko rating of the player at the time of this rating history entry.
     *
     * @return the Glicko rating
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player at the time of this rating history entry.
     *
     * @param glickoRating the Glicko rating
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the rating deviation of the player at the time of this rating history entry.
     *
     * @return the rating deviation
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the rating deviation of the player at the time of this rating history entry.
     *
     * @param ratingDeviation the rating deviation
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the volatility of the player's rating at the time of this rating history entry.
     *
     * @return the volatility
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the volatility of the player's rating at the time of this rating history entry.
     *
     * @param volatility the volatility
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    /**
     * Gets the date and time when this rating history entry was recorded.
     *
     * @return the date and time of the rating history entry
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date and time when this rating history entry was recorded.
     *
     * @param date the date and time of the rating history entry
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
