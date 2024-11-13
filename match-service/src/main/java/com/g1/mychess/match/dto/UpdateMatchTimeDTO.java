package com.g1.mychess.match.dto;

import java.time.LocalDateTime;

public class UpdateMatchTimeDTO {
    private LocalDateTime scheduledTime;

    // Getter and Setter
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}