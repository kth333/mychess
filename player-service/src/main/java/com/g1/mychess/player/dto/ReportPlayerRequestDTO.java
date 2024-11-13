package com.g1.mychess.player.dto;

public class ReportPlayerRequestDTO {
    private String reportedPlayerUsername;
    private String reporterPlayerUsername;
    private String reason;
    private String description;

    public String getReportedPlayerUsername() {
        return reportedPlayerUsername;
    }

    public void setReportedPlayerUsername(String reportedPlayerUsername) {
        this.reportedPlayerUsername = reportedPlayerUsername;
    }

    public String getReporterPlayerUsername() {
        return reporterPlayerUsername;
    }

    public void setReporterPlayerUsername(String reporterPlayerUsername) {
        this.reporterPlayerUsername = reporterPlayerUsername;
    }

    public String getReason() {
    return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }
}
