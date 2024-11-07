package com.g1.mychess.match.dto;

public class MatchResultDTO {
    private Long winnerId;
    private Long loserId;
    private boolean isDraw;

    // Constructors, getters, and setters

    public MatchResultDTO() {}

    public MatchResultDTO(Long winnerId, Long loserId, boolean isDraw) {
        this.winnerId = winnerId;
        this.loserId = loserId;
        this.isDraw = isDraw;
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

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }
}