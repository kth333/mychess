package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.LeaderboardProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileUpdateDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.dto.PlayerRatingHistoryDTO;
import com.g1.mychess.player.service.PlayerRatingHistoryService;
import com.g1.mychess.player.service.ProfileService;
import com.g1.mychess.player.util.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class that handles API requests related to player profiles, including
 * fetching player profile details, updating ratings, updating profiles, and retrieving
 * rating history and leaderboard information.
 */
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final PlayerRatingHistoryService playerRatingHistoryService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor to initialize ProfileController.
     *
     * @param profileService the service used to manage player profiles
     * @param playerRatingHistoryService the service used to manage player rating history
     * @param jwtUtil utility class for handling JWT operations
     */
    public ProfileController(ProfileService profileService, PlayerRatingHistoryService playerRatingHistoryService, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.playerRatingHistoryService = playerRatingHistoryService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Fetches the profile of a player by their player ID.
     *
     * @param playerId the ID of the player whose profile is requested
     * @return a ResponseEntity containing the player profile data
     */
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerProfileDTO> getProfileById(@PathVariable Long playerId) {
        return ResponseEntity.ok(profileService.getPlayerProfile(playerId));
    }

    /**
     * Updates the rating of a player.
     *
     * @param ratingUpdateDTO the DTO containing the updated rating information
     * @return a ResponseEntity indicating the success of the update
     */
    @PostMapping("/rating")
    public ResponseEntity<Void> updateProfileRating(@Valid @RequestBody PlayerRatingUpdateDTO ratingUpdateDTO) {
        profileService.updateProfileRating(ratingUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Updates the profile information of a player.
     *
     * @param playerId the ID of the player whose profile is to be updated
     * @param profileUpdateDTO the DTO containing the updated profile data
     * @return a ResponseEntity with the updated profile status
     */
    @PutMapping("/{playerId}")
    public ResponseEntity<String> updatePlayerProfile(@Valid @PathVariable Long playerId, @RequestBody PlayerProfileUpdateDTO profileUpdateDTO) {
        return profileService.updatePlayerProfile(playerId, profileUpdateDTO);
    }

    /**
     * Retrieves the rating history of a player.
     *
     * @param playerId the ID of the player whose rating history is requested
     * @return a ResponseEntity containing the player's rating history
     */
    @GetMapping("/rating-history/{playerId}")
    public ResponseEntity<List<PlayerRatingHistoryDTO>> getPlayerRatingHistory(@Valid @PathVariable Long playerId) {
        return playerRatingHistoryService.getPlayerRatingHistory(playerId);
    }

    /**
     * Fetches the leaderboard with a list of player profiles.
     *
     * @return a ResponseEntity containing the leaderboard data
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardProfileDTO>> getLeaderboard() {
        return ResponseEntity.ok(profileService.getLeaderboard());
    }
}
