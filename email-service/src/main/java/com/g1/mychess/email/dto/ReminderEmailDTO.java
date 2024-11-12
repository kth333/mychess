package com.g1.mychess.email.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for sending reminder emails to match participants.
 * Contains necessary information about the match and recipient email address.
 */
public class ReminderEmailDTO {

    /**
     * The recipient's email address.
     */
    private String to;

    /**
     * The unique identifier of the match.
     */
    private String tournamentName;

    /**
     * The scheduled time of the match.
     */
    private LocalDateTime scheduledTime;

    /**
     * Retrieves the recipient's email address.
     *
     * @return the email address of the recipient
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's email address.
     *
     * @param to the email address of the recipient
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Retrieves the match ID.
     *
     * @return the unique identifier of the match
     */
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * Sets the match ID.
     *
     * @param tournamentName the unique identifier of the match
     */
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     * Retrieves the scheduled time of the match.
     *
     * @return the scheduled time of the match
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Sets the scheduled time of the match.
     *
     * @param scheduledTime the scheduled time of the match
     */
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
