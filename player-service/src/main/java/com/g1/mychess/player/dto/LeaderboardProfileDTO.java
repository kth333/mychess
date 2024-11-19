package com.g1.mychess.player.dto;

import com.g1.mychess.player.model.CustomChessRank;

/**
 * Data Transfer Object (DTO) representing a player's profile in the leaderboard.
 * This class encapsulates the player's ID, username, rank, country, and Glicko rating
 * for displaying leaderboard information.
 */
public class LeaderboardProfileDTO {

    private Long playerId;
    private String username;
    private CustomChessRank rank;
    private String country;
    private double glickoRating;

    /**
     * Constructs a LeaderboardProfileDTO with the player's ID, username, rank, country,
     * and Glicko rating.
     *
     * @param playerId the unique identifier for the player
     * @param username the player's username
     * @param rank the player's custom chess rank
     * @param country the player's country
     * @param glickoRating the player's Glicko rating
     */
    public LeaderboardProfileDTO(Long playerId, String username, CustomChessRank rank, String country, double glickoRating) {
        this.playerId = playerId;
        this.username = username;
        this.rank = rank;
        this.country = country;
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the player's unique identifier.
     *
     * @return the player ID
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param playerId the player ID to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the player's username.
     *
     * @return the player's username
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
     * Gets the player's custom chess rank.
     *
     * @return the player's rank
     */
    public CustomChessRank getRank() {
        return rank;
    }

    /**
     * Sets the player's custom chess rank.
     *
     * @param rank the rank to set
     */
    public void setRank(CustomChessRank rank) {
        this.rank = rank;
    }

    /**
     * Gets the player's country.
     *
     * @return the player's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the player's country.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the player's Glicko rating.
     *
     * @return the Glicko rating
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
}
