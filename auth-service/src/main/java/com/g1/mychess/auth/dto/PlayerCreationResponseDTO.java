package com.g1.mychess.auth.dto;

/**
 * Data Transfer Object representing the response after a player is created.
 * Contains the player ID and a message about the result of the creation process.
 */
public class PlayerCreationResponseDTO {

    /**
     * The unique identifier for the player.
     */
    private Long playerId;

    /**
     * A message describing the result of the player creation process.
     */
    private String message;

    /**
     * Constructs a new PlayerCreationResponseDTO with the specified player ID and message.
     *
     * @param playerId the unique identifier for the player
     * @param message the message describing the result of the creation process
     */
    public PlayerCreationResponseDTO(Long playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    /**
     * Gets the unique identifier for the player.
     *
     * @return the player ID
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the unique identifier for the player.
     *
     * @param playerId the player ID to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the message describing the result of the player creation process.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message describing the result of the player creation process.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
