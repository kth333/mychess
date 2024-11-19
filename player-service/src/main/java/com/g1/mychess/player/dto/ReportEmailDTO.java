package com.g1.mychess.player.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) class for reporting a player via email. This class contains
 * the necessary details for an email report, including the usernames of the reporter and the reported player,
 * the reason for the report, and a description of the incident.
 */
public class ReportEmailDTO {

    @NotNull
    private String reporterUsername;

    @NotNull
    private String reportedPlayerUsername;

    @NotNull
    private String reason;

    private String description;

    /**
     * Constructs a ReportEmailDTO with the given parameters.
     *
     * @param reporterUsername the username of the player making the report
     * @param reportedPlayerUsername the username of the player being reported
     * @param reason the reason for reporting the player
     * @param description a detailed description of the incident
     */
    public ReportEmailDTO(String reporterUsername, String reportedPlayerUsername, String reason, String description) {
        this.reporterUsername = reporterUsername;
        this.reportedPlayerUsername = reportedPlayerUsername;
        this.reason = reason;
        this.description = description;
    }

    /**
     * Gets the username of the player making the report.
     *
     * @return the username of the reporter player
     */
    public String getReporterUsername() {
        return reporterUsername;
    }

    /**
     * Gets the username of the player being reported.
     *
     * @return the username of the reported player
     */
    public String getReportedPlayerUsername() {
        return reportedPlayerUsername;
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
     * Gets the description of the incident that led to the report.
     *
     * @return the description of the report
     */
    public String getDescription() {
        return description;
    }
}
