package com.g1.mychess.player.dto;

public class PlayerCreationResponseDTO {
    private Long playerId;
    private String message;

    // Constructor, Getters, and Setters
    public PlayerCreationResponseDTO(Long playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
