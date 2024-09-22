package com.g1.mychess.admin.dto;

public class WhitelistEmailDTO {

    private String to;
    private String username;
    private String reason;

    public WhitelistEmailDTO(String to, String username, String reason) {
        this.to = to;
        this.username = username;
        this.reason = reason;
    }

    public WhitelistEmailDTO() {

    }

    // Getters and Setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}