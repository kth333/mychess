package com.g1.mychess.admin.dto;

/**
 * Data Transfer Object (DTO) used for holding email-related information
 * about the whitelisting process. This DTO is used to send details about
 * the whitelisted player, including the recipient email, the username of
 * the player, and the reason for their whitelisting.
 */
public class WhitelistEmailDTO {

    private String to; // The recipient's email address
    private String username; // The username of the player being whitelisted
    private String reason; // The reason for the player's whitelisting

    /**
     * Constructor for initializing WhitelistEmailDTO with all fields.
     *
     * @param to the recipient email address
     * @param username the username of the player being whitelisted
     * @param reason the reason for the whitelisting
     */
    public WhitelistEmailDTO(String to, String username, String reason) {
        this.to = to;
        this.username = username;
        this.reason = reason;
    }

    /**
     * Default constructor for WhitelistEmailDTO.
     */
    public WhitelistEmailDTO() {

    }

    // Getters and Setters

    /**
     * Gets the recipient email address.
     *
     * @return the email address of the recipient
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient email address.
     *
     * @param to the email address to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the username of the player being whitelisted.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the player being whitelisted.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the reason for the player's whitelisting.
     *
     * @return the reason for the whitelisting
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the player's whitelisting.
     *
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}