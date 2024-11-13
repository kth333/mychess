package com.g1.mychess.match.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) for representing the result of a chess match.
 * This class contains details about the match ID, winner ID, loser ID, and whether the match was a draw.
 */
public class MatchResultDTO {

    /**
     * The unique identifier of the match whose result is being recorded.
     */
    private Long matchId;

    /**
     * The unique identifier of the player who won the match.
     */
    private Long winnerId;

    /**
     * The unique identifier of the player who lost the match.
     */
    private Long loserId;

    /**
     * A boolean flag indicating if the match ended in a draw.
     * This property is mapped to the JSON field "isDraw".
     */
    @JsonProperty("isDraw")
    private boolean isDraw;

    // Constructors

    /**
     * Default constructor for creating an empty MatchResultDTO object.
     */
    public MatchResultDTO() {}

    /**
     * Constructor for creating a MatchResultDTO with the specified match result details.
     *
     * @param matchId The unique identifier for the match.
     * @param winnerId The ID of the player who won the match.
     * @param loserId The ID of the player who lost the match.
     * @param isDraw A boolean indicating if the match was a draw.
     */
    public MatchResultDTO(Long matchId, Long winnerId, Long loserId, boolean isDraw) {
        this.matchId = matchId;
        this.winnerId = winnerId;
        this.loserId = loserId;
        this.isDraw = isDraw;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier of the match.
     *
     * @return The match ID.
     */
    public Long getMatchId() {
        return matchId;
    }

    /**
     * Sets the unique identifier of the match.
     *
     * @param matchId The match ID to set.
     */
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    /**
     * Gets the unique identifier of the player who won the match.
     *
     * @return The winner's ID.
     */
    public Long getWinnerId() {
        return winnerId;
    }

    /**
     * Sets the unique identifier of the player who won the match.
     *
     * @param winnerId The winner's ID to set.
     */
    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    /**
     * Gets the unique identifier of the player who lost the match.
     *
     * @return The loser's ID.
     */
    public Long getLoserId() {
        return loserId;
    }

    /**
     * Sets the unique identifier of the player who lost the match.
     *
     * @param loserId The loser's ID to set.
     */
    public void setLoserId(Long loserId) {
        this.loserId = loserId;
    }

    /**
     * Gets the boolean flag indicating whether the match was a draw.
     *
     * @return True if the match was a draw, false otherwise.
     */
    public boolean getIsDraw() {
        return isDraw;
    }

    /**
     * Sets the boolean flag indicating whether the match was a draw.
     *
     * @param isDraw The value to set for the draw flag.
     */
    public void setIsDraw(boolean isDraw) {
        this.isDraw = isDraw;
    }
}
