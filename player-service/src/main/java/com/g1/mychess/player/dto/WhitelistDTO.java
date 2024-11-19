package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) class for transferring information about a player's whitelist status.
 * This class contains details such as player ID, username, email, and the reason for being whitelisted.
 */
public class WhitelistDTO {

    private Long playerId;
    private String username;
    private String email;
    private String reason;

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
     * @param playerId the player ID to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the username of the player.
     *
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the player.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email of the player.
     *
     * @return the player's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the player.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the reason for whitelisting the player.
     *
     * @return the reason for whitelisting
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for whitelisting the player.
     *
     * @param reason the reason for whitelisting to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}
