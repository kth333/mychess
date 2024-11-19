package com.g1.mychess.tournament.dto;

/**
 * Data Transfer Object representing a tournament notification.
 */
public class TournamentNotificationDTO {

    private String to;
    private String subject;
    private String message;

    /**
     * Default constructor.
     */
    public TournamentNotificationDTO() {
    }

    /**
     * Constructs a new {@code TournamentNotificationDTO} with the specified details.
     *
     * @param to      the recipient of the notification
     * @param subject the subject of the notification
     * @param message the message content of the notification
     */
    public TournamentNotificationDTO(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    /**
     * Returns the recipient of the notification.
     *
     * @return the recipient of the notification
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient of the notification.
     *
     * @param to the recipient of the notification
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Returns the subject of the notification.
     *
     * @return the subject of the notification
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the notification.
     *
     * @param subject the subject of the notification
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the message content of the notification.
     *
     * @return the message content of the notification
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content of the notification.
     *
     * @param message the message content of the notification
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
