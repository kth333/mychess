package com.g1.mychess.email.dto;

public class BlacklistEmailDTO {

    private String to;
    private String username;
    private boolean isBlacklisted;
    private String reason;
    private Long banDuration;

    public BlacklistEmailDTO(String to, String username, boolean isBlacklisted, String reason, Long banDuration) {
        this.to = to;
        this.username = username;
        this.isBlacklisted = isBlacklisted;
        this.reason = reason;
        this.banDuration = banDuration;
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

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getBanDuration() {
        return banDuration;
    }

    public void setBanDuration(Long banDuration) {
        this.banDuration = banDuration;
    }
}