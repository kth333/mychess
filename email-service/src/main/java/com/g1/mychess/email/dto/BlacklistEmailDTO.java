package com.g1.mychess.email.dto;

/**
 * Data Transfer Object (DTO) for the blacklist email details.
 * This class is used to encapsulate the information needed for sending a blacklist notification email.
 */
public class BlacklistEmailDTO {

    /**
     * The recipient's email address for the blacklist notification.
     * This field stores the email address of the user who is being notified of the blacklist.
     */
    private String to;

    /**
     * The username of the person being blacklisted.
     * This field stores the username of the user who is being notified of their blacklist status.
     */
    private String username;

    /**
     * The reason for the user being blacklisted.
     * This field stores the reason why the user has been blacklisted.
     */
    private String reason;

    /**
     * The duration of the user's ban in hours.
     * This field stores the length of time (in hours) for which the user is banned.
     */
    private Long banDuration;

    /**
     * Constructs a BlacklistEmailDTO with the specified recipient, username, reason, and ban duration.
     *
     * @param to The recipient's email address.
     * @param username The username of the blacklisted person.
     * @param reason The reason for the blacklist.
     * @param banDuration The duration (in hours) of the ban.
     */
    public BlacklistEmailDTO(String to, String username, String reason, Long banDuration) {
        this.to = to;
        this.username = username;
        this.reason = reason;
        this.banDuration = banDuration;
    }

    /**
     * Gets the recipient's email address.
     *
     * @return The recipient's email address.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's email address.
     *
     * @param to The recipient's email address.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the username of the blacklisted person.
     *
     * @return The username of the blacklisted person.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the blacklisted person.
     *
     * @param username The username of the blacklisted person.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the reason for the blacklist.
     *
     * @return The reason for the blacklist.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the blacklist.
     *
     * @param reason The reason for the blacklist.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the duration of the user's ban in hours.
     *
     * @return The duration of the ban in hours.
     */
    public Long getBanDuration() {
        return banDuration;
    }

    /**
     * Sets the duration of the user's ban in hours.
     *
     * @param banDuration The duration of the ban in hours.
     */
    public void setBanDuration(Long banDuration) {
        this.banDuration = banDuration;
    }
}