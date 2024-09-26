package com.g1.mychess.player.dto;

import java.time.LocalDate;
import com.g1.mychess.player.model.CustomChessRank;

public class PrivatePlayerProfileDTO {

    private Long playerId;
    private Integer wonMatches; // Nullable field for won matches
    private Integer lostMatches; // Nullable field for lost matches
    private LocalDate lastLoginDate; // Nullable field for last login
    private CustomChessRank chessRank; // Nullable field for chess rank

    public PrivatePlayerProfileDTO(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    // Getters and setters for nullable fields
    public Integer getWonMatches() {
        return wonMatches;
    }

    public void setWonMatches(Integer wonMatches) {
        this.wonMatches = wonMatches;
    }

    public Integer getLostMatches() {
        return lostMatches;
    }

    public void setLostMatches(Integer lostMatches) {
        this.lostMatches = lostMatches;
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public CustomChessRank getChessRank() {
        return chessRank;
    }

    public void setChessRank(CustomChessRank chessRank) {
        this.chessRank = chessRank;
    }
}
