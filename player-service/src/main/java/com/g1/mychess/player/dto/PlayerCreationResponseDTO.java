package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) representing the response after a player is created.
 * This class encapsulates the player's unique ID and a message associated with
 * the creation process.
 */
public class PlayerCreationResponseDTO {

    private Long playerId;
    private String message;

    /**
     * Constructs a PlayerCreationResponseDTO with the player's ID and a message.
     *
     * @param playerId the unique identifier for the created player
     * @param message a message indicating the result or status of the player creation
     */
    public PlayerCreationResponseDTO(Long playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    /**
     * Gets the player's unique identifier.
     *
     * @return the player ID
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param playerId the player ID to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the message associated with the player creation process.
     *
     * @return the message regarding the player creation
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message associated with the player creation process.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
