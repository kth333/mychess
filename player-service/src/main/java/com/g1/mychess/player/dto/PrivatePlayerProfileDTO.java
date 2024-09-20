package com.g1.mychess.player.dto;

import java.time.LocalDate;

import com.g1.mychess.player.model.CustomChessRank;

public class PrivatePlayerProfileDTO {

    private Long playerId;

    public PrivatePlayerProfileDTO(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
