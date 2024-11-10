package com.g1.mychess.auth.dto;

/**
 * Data Transfer Object representing a request to update a user's password.
 * It includes the player ID and the new password.
 */
public class UpdatePasswordRequestDTO {

    /**
     * The unique identifier for the player whose password is being updated.
     */
    private Long playerId;

    /**
     * The new password that the player has chosen.
     */
    private String newPassword;

    /**
     * Constructs an UpdatePasswordRequestDTO with the provided player ID and new password.
     *
     * @param playerId the unique identifier of the player whose password is being updated
     * @param newPassword the new password chosen by the player
     */
    public UpdatePasswordRequestDTO(Long playerId, String newPassword) {
        this.playerId = playerId;
        this.newPassword = newPassword;
    }

    /**
     * Gets the unique identifier of the player.
     *
     * @return the player ID
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the unique identifier of the player.
     *
     * @param playerId the player ID to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the new password chosen by the player.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password for the player.
     *
     * @param newPassword the new password to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
