package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) class for reporting a player. This class contains details
 * about the player being reported, the player who is reporting, the reason for the report,
 * and a description of the incident.
 */
public class ReportPlayerRequestDTO {

    private String reportedPlayerUsername;
    private String reporterPlayerUsername;
    private String reason;
    private String description;

    /**
     * Gets the username of the player being reported.
     *
     * @return the username of the reported player
     */
    public String getReportedPlayerUsername() {
        return reportedPlayerUsername;
    }

    /**
     * Sets the username of the player being reported.
     *
     * @param reportedPlayerUsername the username of the reported player
     */
    public void setReportedPlayerUsername(String reportedPlayerUsername) {
        this.reportedPlayerUsername = reportedPlayerUsername;
    }

    /**
     * Gets the username of the player making the report.
     *
     * @return the username of the reporter player
     */
    public String getReporterPlayerUsername() {
        return reporterPlayerUsername;
    }

    /**
     * Sets the username of the player making the report.
     *
     * @param reporterPlayerUsername the username of the reporter player
     */
    public void setReporterPlayerUsername(String reporterPlayerUsername) {
        this.reporterPlayerUsername = reporterPlayerUsername;
    }

    /**
     * Gets the reason for the report.
     *
     * @return the reason for reporting the player
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the report.
     *
     * @param reason the reason for reporting the player
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the description of the incident that led to the report.
     *
     * @return the description of the report
     */
    public String getDescription() {
        return description;
    }
}
