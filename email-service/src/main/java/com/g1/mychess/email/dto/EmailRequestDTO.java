package com.g1.mychess.email.dto;

/**
 * Data Transfer Object (DTO) for representing an email request.
 * This class encapsulates the details required to send an email, including the recipient's information and a user-specific token.
 */
public class EmailRequestDTO {

    /**
     * The recipient's email address.
     */
    private String to;

    /**
     * The username associated with the email request.
     */
    private String username;

    /**
     * The user token, typically used for verification or other user-related operations.
     */
    private String userToken;

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
     * Gets the username associated with the email request.
     *
     * @return The username of the user related to the email request.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with the email request.
     *
     * @param username The username of the user related to the email request.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user token, typically used for verification or other user-related operations.
     *
     * @return The user token.
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * Sets the user token, typically used for verification or other user-related operations.
     *
     * @param userToken The user token.
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}