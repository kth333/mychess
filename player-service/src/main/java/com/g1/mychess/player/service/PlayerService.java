package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;


/**
 * Service interface for managing players in the system.
 * Provides methods for creating, updating, retrieving, and interacting with players.
 */
public interface PlayerService {

    /**
     * Creates a new player in the system.
     *
     * @param registerRequestDTO the DTO containing the details for registering a new player.
     * @return a ResponseEntity containing the response information after the player creation, including player details.
     */
    ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO registerRequestDTO);

    /**
     * Updates the password of an existing player.
     *
     * @param playerId the ID of the player whose password is to be updated.
     * @param newPassword the new password to be set for the player.
     */
    void updatePlayerPassword(Long playerId, String newPassword);

    /**
     * Finds a player by their username.
     *
     * @param username the username of the player to be found.
     * @return a UserDTO containing the player's details.
     */
    UserDTO findPlayerByUsername(String username);

    /**
     * Finds a player by their player ID.
     *
     * @param playerId the ID of the player to be found.
     * @return a UserDTO containing the player's details.
     */
    UserDTO findPlayerById(Long playerId);

    /**
     * Finds a player by their email address.
     *
     * @param email the email address of the player to be found.
     * @return a UserDTO containing the player's details.
     */
    UserDTO findPlayerByEmail(String email);

    /**
     * Retrieves detailed information about a player.
     *
     * @param playerId the ID of the player whose details are to be fetched.
     * @return a PlayerDTO containing detailed player information.
     */
    PlayerDTO getPlayerDetails(Long playerId);

    /**
     * Allows a player to report another player.
     *
     * @param reportPlayerRequestDTO the DTO containing the details of the report.
     * @return a ResponseEntity containing the response message after reporting the player.
     */
    ResponseEntity<String> reportPlayer(ReportPlayerRequestDTO reportPlayerRequestDTO);

    /**
     * Blacklists a player, preventing them from participating in certain activities.
     *
     * @param playerId the ID of the player to be blacklisted.
     */
    void blacklistPlayer(Long playerId);

    /**
     * Removes a player from the blacklist, allowing them to participate in activities again.
     *
     * @param playerId the ID of the player to be whitelisted.
     */
    void whitelistPlayer(Long playerId);

    /**
     * Searches for players based on a query string.
     *
     * @param query the search query string to find players by.
     * @param page the page number for pagination.
     * @param size the number of players per page.
     * @return a Page containing a list of PlayerDTO objects for the search results.
     */
    Page<PlayerDTO> searchPlayers(String query, int page, int size);

    /**
     * Allows a player to follow another player.
     *
     * @param followerId the ID of the player who is following.
     * @param followedId the ID of the player who is being followed.
     * @return a ResponseEntity containing the response message after following the player.
     */
    ResponseEntity<String> followPlayer(Long followerId, Long followedId);

    /**
     * Allows a player to unfollow another player.
     *
     * @param followerId the ID of the player who is unfollowing.
     * @param followedId the ID of the player who is being unfollowed.
     * @return a ResponseEntity containing the response message after unfollowing the player.
     */
    ResponseEntity<String> unfollowPlayer(Long followerId, Long followedId);

    /**
     * Retrieves a list of players followed by a specific player.
     *
     * @param followerId the ID of the player whose followed players are to be retrieved.
     * @param page the page number for pagination.
     * @param size the number of players per page.
     * @return a Page containing a list of PlayerDTO objects for the followed players.
     */
    Page<PlayerDTO> getFollowedPlayers(Long followerId, int page, int size);

    /**
     * Retrieves a list of players following a specific player.
     *
     * @param playerId the ID of the player whose followers are to be retrieved.
     * @param page the page number for pagination.
     * @param size the number of players per page.
     * @return a Page containing a list of PlayerDTO objects for the followers.
     */
    Page<PlayerDTO> getFollowers(Long playerId, int page, int size);
}