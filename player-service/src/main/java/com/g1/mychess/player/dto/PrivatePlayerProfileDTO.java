package com.g1.mychess.player.dto;

import java.time.LocalDate;

import com.g1.mychess.player.model.CustomChessRank;

public class PrivatePlayerProfileDTO {

    private Long playerId;
    private String username;
    private String email;

    public PrivatePlayerProfileDTO(Long playerId, String username, String email) {
        this.playerId = playerId;
        this.username = username;
        this.email = email;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
}
