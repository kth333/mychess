package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.service.PlayerService;
import com.g1.mychess.player.util.JwtUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Controller class responsible for handling player-related API endpoints.
 * This includes player registration, updating player status (blacklist/whitelist),
 * fetching player details, following/unfollowing players, reporting players, and more.
 */
@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {

    private final PlayerService playerService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor for initializing PlayerController.
     *
     * @param playerService the service to manage player-related operations
     * @param jwtUtil the utility class for handling JWT-related tasks
     */
    public PlayerController(PlayerService playerService, JwtUtil jwtUtil) {
        this.playerService = playerService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Creates a new player based on the provided registration information.
     *
     * @param registerRequestDTO the DTO containing the registration data
     * @return a ResponseEntity with the response containing the player's creation status
     */
    @PostMapping("/")
    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return playerService.createPlayer(registerRequestDTO);
    }

    /**
     * Updates the status of a player to be blacklisted.
     *
     * @param playerId the ID of the player to be blacklisted
     * @return a ResponseEntity with the success message
     */
    @PutMapping("/blacklist-status/{playerId}")
    public ResponseEntity<String> updateBlacklistStatus(@PathVariable @Positive Long playerId) {
        playerService.blacklistPlayer(playerId);
        return ResponseEntity.ok("Player has been blacklisted successfully.");
    }

    /**
     * Updates the status of a player to be whitelisted.
     *
     * @param playerId the ID of the player to be whitelisted
     * @return a ResponseEntity with the success message
     */
    @PutMapping("/whitelist-status/{playerId}")
    public ResponseEntity<String> updateWhitelistStatus(@PathVariable @Positive Long playerId) {
        playerService.whitelistPlayer(playerId);
        return ResponseEntity.ok("Player has been whitelisted successfully.");
    }

    /**
     * Fetches the player by their username.
     *
     * @param username the username of the player to be fetched
     * @return a ResponseEntity containing the player information
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getPlayerByUsername(@Valid @PathVariable String username) {
        UserDTO userDTO = playerService.findPlayerByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Fetches the player by their player ID.
     *
     * @param playerId the ID of the player to be fetched
     * @return a ResponseEntity containing the player information
     */
    @GetMapping("/playerId/{playerId}")
    public ResponseEntity<UserDTO> getPlayerById(@Valid @PathVariable Long playerId) {
        UserDTO userDTO = playerService.findPlayerById(playerId);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Fetches the player by their email address.
     *
     * @param email the email address of the player to be fetched
     * @return a ResponseEntity containing the player information
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getPlayerByEmail(@Valid @PathVariable String email) {
        UserDTO userDTO = playerService.findPlayerByEmail(email);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Updates the password of a player.
     *
     * @param updatePasswordRequest the DTO containing the player's ID and new password
     * @return a ResponseEntity with a message indicating the password update status
     */
    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequestDTO updatePasswordRequest) {
        playerService.updatePlayerPassword(updatePasswordRequest.getPlayerId(), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }

    /**
     * Fetches detailed information about a player by their player ID.
     *
     * @param playerId the ID of the player whose details are requested
     * @return a ResponseEntity containing the detailed player information
     */
    @GetMapping("/{playerId}/details")
    public ResponseEntity<PlayerDTO> getPlayerWithDetails(@Valid @PathVariable Long playerId) {
        PlayerDTO playerDTO = playerService.getPlayerDetails(playerId);
        return ResponseEntity.ok(playerDTO);
    }

    /**
     * Reports a player based on the provided report information.
     *
     * @param reportPlayerRequestDTO the DTO containing the report data
     * @return a ResponseEntity with the status of the report
     */
    @PostMapping("/reports")
    public ResponseEntity<String> reportPlayer(@RequestBody ReportPlayerRequestDTO reportPlayerRequestDTO) {
        return playerService.reportPlayer(reportPlayerRequestDTO);
    }

    /**
     * Searches for players based on the provided query string, with pagination support.
     *
     * @param query the search query
     * @param page the page number for pagination (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a ResponseEntity containing the paginated result of player search
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PlayerDTO>> searchPlayers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PlayerDTO> result = playerService.searchPlayers(query, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Allows a player to follow another player.
     *
     * @param followerId the ID of the player who wants to follow
     * @param followedId the ID of the player being followed
     * @return a ResponseEntity with the status of the follow operation
     */
    @PostMapping("/{followerId}/follow/{followedId}")
    public ResponseEntity<String> followPlayer(@PathVariable Long followerId, @PathVariable Long followedId) {
        return playerService.followPlayer(followerId, followedId);
    }

    /**
     * Allows a player to unfollow another player.
     *
     * @param followerId the ID of the player who wants to unfollow
     * @param followedId the ID of the player being unfollowed
     * @return a ResponseEntity with the status of the unfollow operation
     */
    @DeleteMapping("/{followerId}/unfollow/{followedId}")
    public ResponseEntity<String> unfollowPlayer(@PathVariable Long followerId, @PathVariable Long followedId) {
        return playerService.unfollowPlayer(followerId, followedId);
    }

    /**
     * Retrieves a paginated list of players following a given player.
     *
     * @param playerId the ID of the player whose followers are being requested
     * @param page the page number for pagination (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a ResponseEntity containing the paginated list of followers
     */
    @GetMapping("/{playerId}/followers")
    public ResponseEntity<Page<PlayerDTO>> getFollowers(
            @PathVariable Long playerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PlayerDTO> followers = playerService.getFollowers(playerId, page, size);
        return ResponseEntity.ok(followers);
    }

    /**
     * Retrieves a paginated list of players followed by a given player.
     *
     * @param playerId the ID of the player whose following list is being requested
     * @param page the page number for pagination (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a ResponseEntity containing the paginated list of followed players
     */
    @GetMapping("/{playerId}/following")
    public ResponseEntity<Page<PlayerDTO>> getFollowing(
            @PathVariable Long playerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PlayerDTO> following = playerService.getFollowedPlayers(playerId, page, size);
        return ResponseEntity.ok(following);
    }

    /**
     * Simple health check endpoint to verify that the service is up and running.
     *
     * @return a ResponseEntity containing a message indicating the health status of the service
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}
