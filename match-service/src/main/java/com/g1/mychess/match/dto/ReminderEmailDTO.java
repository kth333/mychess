package com.g1.mychess.match.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for holding match reminder email details.
 * This object is used to encapsulate the data required for sending a match reminder email.
 */
public class ReminderEmailDTO {

    /**
     * The recipient's email address where the reminder email will be sent.
     */
    private String to;

    /**
     * The name of the tournament associated with the match.
     */
    private String tournamentName;

    /**
     * The scheduled time for the match that is being reminded about.
     */
    private LocalDateTime scheduledTime;

    // Getters and Setters

    /**
     * Gets the recipient's email address.
     *
     * @return the recipient's email address.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's email address.
     *
     * @param to the recipient's email address.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the name of the tournament.
     *
     * @return the name of the tournament.
     */
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * Sets the name of the tournament.
     *
     * @param tournamentName the name of the tournament.
     */
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     * Gets the scheduled time for the match.
     *
     * @return the scheduled time of the match.
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Sets the scheduled time for the match.
     *
     * @param scheduledTime the scheduled time of the match.
     */
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}

