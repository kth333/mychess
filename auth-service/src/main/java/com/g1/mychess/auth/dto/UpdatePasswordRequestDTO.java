package com.g1.mychess.auth.dto;

public class UpdatePasswordRequestDTO {
    private Long playerId;
    private String newPassword;

    public UpdatePasswordRequestDTO(Long playerId, String newPassword) {
        this.playerId = playerId;
        this.newPassword = newPassword;
    }

    public Long getplayerId() {
        return playerId;
    }

    public void setplayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
