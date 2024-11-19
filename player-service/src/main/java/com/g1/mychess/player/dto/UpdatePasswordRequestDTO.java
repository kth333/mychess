package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) class for updating the password of a player.
 * This class contains the player's ID and the new password to be set.
 */
public class UpdatePasswordRequestDTO {

    private Long playerId;
    private String newPassword;

    /**
     * Constructs a new UpdatePasswordRequestDTO with the specified player ID and new password.
     *
     * @param playerId the ID of the player whose password is being updated
     * @param newPassword the new password to set for the player
     */
    public UpdatePasswordRequestDTO(Long playerId, String newPassword) {
        this.playerId = playerId;
        this.newPassword = newPassword;
    }

    /**
     * Gets the player ID.
     *
     * @return the player ID
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player ID.
     *
     * @param playerId the player ID to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the new password.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password.
     *
     * @param newPassword the new password to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
