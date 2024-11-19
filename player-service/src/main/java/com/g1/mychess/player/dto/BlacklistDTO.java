package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) representing a player's blacklist status.
 * This class encapsulates the player's ID, username, email, the reason for being blacklisted,
 * and the duration of the ban.
 */
public class BlacklistDTO {

    private Long playerId;
    private String username;
    private String email;
    private String reason;
    private Long banDuration;  // In hours

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
     * Gets the player's email address.
     *
     * @return the player's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the player's email address.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the reason why the player was blacklisted.
     *
     * @return the reason for blacklisting
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason why the player was blacklisted.
     *
     * @param reason the reason for blacklisting
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the duration of the player's ban in hours.
     *
     * @return the ban duration in hours
     */
    public Long getBanDuration() {
        return banDuration;
    }

    /**
     * Sets the duration of the player's ban in hours.
     *
     * @param banDuration the ban duration in hours to set
     */
    public void setBanDuration(Long banDuration) {
        this.banDuration = banDuration;
    }
}
