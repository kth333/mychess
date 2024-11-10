package com.g1.mychess.auth.dto;

/**
 * Data Transfer Object representing an email request, including the recipient,
 * username, and user token required for the email content.
 */
public class EmailRequestDTO {

    /**
     * The recipient's email address.
     */
    private String to;

    /**
     * The username associated with the recipient.
     */
    private String username;

    /**
     * The user token for authentication or verification purposes.
     */
    private String userToken;

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
     * @param to the recipient's email address
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the username associated with the email recipient.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with the email recipient.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user token used for authentication or verification.
     *
     * @return the user token
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * Sets the user token used for authentication or verification.
     *
     * @param userToken the user token
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
