package com.g1.mychess.player.dto;

public class UpdatePasswordRequestDTO {
    private Long playerId;
    private String newPassword;

    public UpdatePasswordRequestDTO(Long playerId, String newPassword) {
        this.playerId = playerId;
        this.newPassword = newPassword;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
