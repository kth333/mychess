package com.g1.mychess.email.dto;

/**
 * Data Transfer Object (DTO) for representing the information required to send a tournament notification email.
 * This class is used to encapsulate the details needed to send an email regarding a tournament.
 */
public class TournamentNotificationDTO {

    /**
     * The recipient email address.
     */
    private String to;

    /**
     * The subject of the tournament notification email.
     */
    private String subject;

    /**
     * The message content of the tournament notification email.
     */
    private String message;

    /**
     * Default constructor for {@link TournamentNotificationDTO}.
     * Initializes an empty object.
     */
    public TournamentNotificationDTO() {
    }

    /**
     * Constructs a new {@link TournamentNotificationDTO} with the given details.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the tournament notification email.
     * @param message The message content of the tournament notification email.
     */
    public TournamentNotificationDTO(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
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
     * Gets the subject of the tournament notification email.
     *
     * @return The subject of the tournament notification email.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the tournament notification email.
     *
     * @param subject The subject of the tournament notification email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the message content of the tournament notification email.
     *
     * @return The message content of the tournament notification email.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content of the tournament notification email.
     *
     * @param message The message content of the tournament notification email.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}