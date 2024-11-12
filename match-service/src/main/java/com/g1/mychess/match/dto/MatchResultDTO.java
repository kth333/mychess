package com.g1.mychess.match.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchResultDTO {
    private Long matchId;
    private Long winnerId;
    private Long loserId;

    @JsonProperty("isDraw")
    private boolean isDraw;

    // Constructors, getters, and setters

    public MatchResultDTO() {}

    public MatchResultDTO(Long matchId, Long winnerId, Long loserId, boolean isDraw) {
        this.matchId = matchId;
        this.winnerId = winnerId;
        this.loserId = loserId;
        this.isDraw = isDraw;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public Long getLoserId() {
        return loserId;
    }

    public void setLoserId(Long loserId) {
        this.loserId = loserId;
    }

    public boolean getIsDraw() {
        return isDraw;
    }

    public void setIsDraw(boolean isDraw) {
        this.isDraw = isDraw;
    }
}
