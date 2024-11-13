package com.g1.mychess.player.dto;

public class ReportPlayerRequestDTO {
    private Long reportedPlayerId;
    private Long reporterPlayerId;
    private String reason;
    private String description;

    public Long getReportedPlayerId() {
        return reportedPlayerId;
    }

    public void setReportedPlayerId(Long reportedPlayerId) {
        this.reportedPlayerId = reportedPlayerId;
    }

    public Long getReporterPlayerId() {
        return reporterPlayerId;
    }

    public void setReporterPlayerId(Long reporterPlayerId) {
        this.reporterPlayerId = reporterPlayerId;
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
