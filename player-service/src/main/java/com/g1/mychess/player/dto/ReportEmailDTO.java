package com.g1.mychess.player.dto;

import jakarta.validation.constraints.NotNull;

public class ReportEmailDTO {
    @NotNull
    private String reporterUsername;
    @NotNull
    private String reportedPlayerUsername;
    @NotNull
    private String reason;
    private String description;

    public ReportEmailDTO(String reporterUsername, String reportedPlayerUsername, String reason, String description) {
        this.reporterUsername = reporterUsername;
        this.reportedPlayerUsername = reportedPlayerUsername;
        this.reason = reason;
        this.description = description;
    }

    public String getReporterUsername() {
        return reporterUsername;
    }

    public String getReportedPlayerUsername() {
        return reportedPlayerUsername;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }
}