package com.g1.mychess.match.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for updating the scheduled time of a match.
 * This class is used to carry the updated scheduled time from the client to the server.
 */
public class UpdateMatchTimeDTO {

    /**
     * The scheduled time for the match.
     * This field will be used to update the time of the match in the system.
     */
    private LocalDateTime scheduledTime;

    // Getter and Setter

    /**
     * Gets the scheduled time of the match.
     *
     * @return The scheduled time for the match.
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Sets the scheduled time for the match.
     *
     * @param scheduledTime The updated scheduled time to be set.
     */
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}