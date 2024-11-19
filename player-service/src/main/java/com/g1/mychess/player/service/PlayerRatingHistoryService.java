package com.g1.mychess.player.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.g1.mychess.player.dto.PlayerRatingHistoryDTO;

import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;

/**
 * Service interface for managing player rating histories.
 * Provides methods to update, fetch, and clean up rating histories of players.
 */
public interface PlayerRatingHistoryService {

    /**
     * Cleans up old player rating histories that are no longer needed.
     * This method is typically called to remove outdated rating data from the system.
     */
    void cleanupOldRatingHistories();

    /**
     * Updates the rating history of a player.
     *
     * @param ratingUpdateDTO the DTO containing the details of the rating update to be applied.
     * @throws IllegalArgumentException if the rating update data is invalid or incomplete.
     */
    void updatePlayerRatingHistory(PlayerRatingUpdateDTO ratingUpdateDTO);

    /**
     * Fetches the rating history of a player by their player ID.
     *
     * @param playerId the ID of the player whose rating history is to be fetched.
     * @return a ResponseEntity containing a list of PlayerRatingHistoryDTO objects representing the player's rating history.
     *         If the player does not exist or has no rating history, an empty list is returned.
     */
    ResponseEntity<List<PlayerRatingHistoryDTO>> getPlayerRatingHistory(Long playerId);

}