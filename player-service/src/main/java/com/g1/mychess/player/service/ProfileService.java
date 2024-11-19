package com.g1.mychess.player.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.g1.mychess.player.dto.LeaderboardProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.dto.PlayerProfileUpdateDTO;

/**
 * Service interface for managing player profiles in the system.
 * Provides methods for updating ratings, retrieving and updating profiles, and accessing the leaderboard.
 */
public interface ProfileService {

    /**
     * Updates the player's rating based on the provided rating update data.
     *
     * @param ratingUpdateDTO the DTO containing the rating update details.
     */
    void updateProfileRating(PlayerRatingUpdateDTO ratingUpdateDTO);

    /**
     * Retrieves the profile of a player by their player ID.
     *
     * @param playerId the ID of the player whose profile is to be retrieved.
     * @return a PlayerProfileDTO containing the player's profile details.
     */
    PlayerProfileDTO getPlayerProfile(Long playerId);

    /**
     * Updates the profile information of a player.
     *
     * @param playerId the ID of the player whose profile is to be updated.
     * @param profileUpdateDTO the DTO containing the updated profile details.
     * @return a ResponseEntity containing the response message after updating the profile.
     */
    ResponseEntity<String> updatePlayerProfile(Long playerId, PlayerProfileUpdateDTO profileUpdateDTO);

    /**
     * Retrieves the leaderboard, which lists profiles based on player ratings.
     *
     * @return a list of LeaderboardProfileDTO objects representing the leaderboard.
     */
    List<LeaderboardProfileDTO> getLeaderboard();
}