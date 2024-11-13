package com.g1.mychess.admin.dto;

/**
 * Data Transfer Object (DTO) for handling the details of a player being whitelisted.
 * This DTO contains the player's information such as ID, username, email, and the reason
 * for their whitelisting. It is used to transfer data between the layers of the application.
 */
public class WhitelistDTO {

    private Long playerId; // The unique ID of the player
    private String username; // The username of the player being whitelisted
    private String email; // The email address of the player being whitelisted
    private String reason; // The reason for whitelisting the player

    /**
     * Constructor to initialize a WhitelistDTO with player ID, email, username, and reason.
     *
     * @param id the ID of the player being whitelisted
     * @param email the email address of the player
     * @param username the username of the player
     * @param reason the reason for whitelisting the player
     */
    public WhitelistDTO(Long id, String email, String username, String reason) {
        this.playerId = id;
        this.email = email;
        this.username = username;
        this.reason = reason;
    }

    // Getters and Setters

    /**
     * Gets the player ID.
     *
     * @return the ID of the player
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player ID.
     *
     * @param playerId the ID to set for the player
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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
     * @param email the email to set for the player
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the reason for whitelisting the player.
     *
     * @return the reason for the player's whitelisting
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for whitelisting the player.
     *
     * @param reason the reason to set for the player's whitelisting
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}