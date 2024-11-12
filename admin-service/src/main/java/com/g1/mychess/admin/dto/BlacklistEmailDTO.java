package com.g1.mychess.admin.dto;

/**
 * Data Transfer Object (DTO) representing an email related to blacklisting a player. It contains
 * information about the recipient email address, the player's username, the reason for blacklisting,
 * and the ban duration.
 */
public class BlacklistEmailDTO {

    private String to;            // Recipient email address
    private String username;      // Username of the blacklisted player
    private String reason;        // Reason for blacklisting the player
    private Long banDuration;     // Duration of the ban (in milliseconds)

    // Full Constructor to initialize BlacklistEmailDTO object with all properties
    /**
     * Constructor to initialize the BlacklistEmailDTO with the email details.
     *
     * @param to the recipient's email address
     * @param username the username of the blacklisted player
     * @param reason the reason for blacklisting the player
     * @param banDuration the duration of the ban in milliseconds
     */
    public BlacklistEmailDTO(String to, String username, String reason, Long banDuration) {
        this.to = to;
        this.username = username;
        this.reason = reason;
        this.banDuration = banDuration;
    }

    // Default constructor for object creation without initializing properties
    public BlacklistEmailDTO() {

    }

    // Getters and Setters

    /**
     * Gets the recipient's email address.
     *
     * @return the recipient's email address
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's email address.
     *
     * @param to the email address to set for the recipient
     */
    public void setTo(String to) {
        this.to = to;
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
     * Gets the reason for blacklisting the player.
     *
     * @return the reason for blacklisting
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
     * Gets the duration of the ban in milliseconds.
     *
     * @return the duration of the ban in milliseconds
     */
    public Long getBanDuration() {
        return banDuration;
    }

    /**
     * Sets the duration of the ban in milliseconds.
     *
     * @param banDuration the duration of the ban to set
     */
    public void setBanDuration(Long banDuration) {
        this.banDuration = banDuration;
    }
}