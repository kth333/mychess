package com.g1.mychess.email.dto;

/**
 * Data Transfer Object (DTO) for representing the information required to send a whitelist notification email.
 * This class is used to encapsulate the details needed to send an email to a user notifying them of their whitelist status.
 */
public class WhitelistEmailDTO {

    /**
     * The recipient email address.
     */
    private String to;

    /**
     * The username of the recipient of the email.
     */
    private String username;

    /**
     * The reason the user has been whitelisted.
     */
    private String reason;

    /**
     * Constructs a new {@link WhitelistEmailDTO} with the given details.
     *
     * @param to The recipient's email address.
     * @param username The username of the recipient.
     * @param reason The reason for whitelisting the user.
     */
    public WhitelistEmailDTO(String to, String username, String reason) {
        this.to = to;
        this.username = username;
        this.reason = reason;
    }

    /**
     * Gets the recipient's email address.
     *
     * @return The email address of the recipient.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's email address.
     *
     * @param to The email address of the recipient.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the username of the recipient.
     *
     * @return The username of the recipient.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the recipient.
     *
     * @param username The username of the recipient.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the reason the user has been whitelisted.
     *
     * @return The reason for whitelisting the user.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason the user has been whitelisted.
     *
     * @param reason The reason for whitelisting the user.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}