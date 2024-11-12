package com.g1.mychess.admin.dto;

/**
 * Data Transfer Object (DTO) representing the blacklisting details of a player.
 * This includes the player's ID, username, email, the reason for blacklisting,
 * and the duration of the ban in hours.
 */
public class BlacklistDTO {

    private Long playerId;        // ID of the player being blacklisted
    private String username;      // Username of the blacklisted player
    private String email;         // Email address of the blacklisted player
    private String reason;        // Reason for blacklisting the player
    private Long banDuration;     // Duration of the ban in hours

    // Getters and Setters

    /**
     * Gets the ID of the player being blacklisted.
     *
     * @return the ID of the player
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the ID of the player being blacklisted.
     *
     * @param playerId the ID to set for the player
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the username of the blacklisted player.
     *
     * @return the username of the blacklisted player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the blacklisted player.
     *
     * @param username the username to set for the blacklisted player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the blacklisted player.
     *
     * @return the email address of the blacklisted player
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the blacklisted player.
     *
     * @param email the email address to set for the blacklisted player
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the reason for blacklisting the player.
     *
     * @return the reason for blacklisting the player
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for blacklisting the player.
     *
     * @param reason the reason to set for blacklisting the player
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the duration of the ban in hours.
     *
     * @return the duration of the ban in hours
     */
    public Long getBanDuration() {
        return banDuration;
    }

    /**
     * Sets the duration of the ban in hours.
     *
     * @param banDuration the duration of the ban to set in hours
     */
    public void setBanDuration(Long banDuration) {
        this.banDuration = banDuration;
    }
}