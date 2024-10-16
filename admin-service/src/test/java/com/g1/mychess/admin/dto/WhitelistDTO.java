package com.g1.mychess.admin.dto;

public class WhitelistDTO {

    private Long playerId;
    private String username;
    private String email;
    private String reason;

    public WhitelistDTO(Long id, String email, String username, String reason) {
        this.playerId = id;
        this.email = email;
        this.username = username;
        this.reason = reason;
    }

    // Getters and Setters

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}