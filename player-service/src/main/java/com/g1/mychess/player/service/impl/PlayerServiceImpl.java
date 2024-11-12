package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.mapper.PlayerMapper;
import com.g1.mychess.player.model.Follow;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.FollowRepository;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.service.PlayerService;
import com.g1.mychess.player.client.EmailServiceClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the PlayerService interface, providing services for managing players,
 * including player creation, updating player details, following/unfollowing players, and player reporting.
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final ProfileRepository profileRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;
    private final EmailServiceClient emailServiceClient;
    private final FollowRepository followRepository;

    /**
     * Constructs a new PlayerServiceImpl.
     *
     * @param playerRepository               The repository for player-related operations.
     * @param playerRatingHistoryRepository  The repository for player rating history-related operations.
     * @param emailServiceClient             The client for sending report emails.
     * @param followRepository               The repository for managing follow/unfollow actions.
     */
    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            ProfileRepository profileRepository,
            PlayerRatingHistoryRepository playerRatingHistoryRepository,
            EmailServiceClient emailServiceClient,
            FollowRepository followRepository
    ) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
    }

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Autowired
    public void setPlayerRatingHistoryRepository(PlayerRatingHistoryRepository playerRatingHistoryRepository) {
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
        this.emailServiceClient = emailServiceClient;
        this.followRepository = followRepository;
    }

    /**
     * Creates a new player and profile, then returns a response indicating success or failure.
     *
     * @param registerRequestDTO The registration details for the new player.
     * @return A ResponseEntity containing the result of the player creation process.
     */
    @Override
    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO registerRequestDTO) {
        if (usernameExists(registerRequestDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new PlayerCreationResponseDTO(null, "Username already exists"));
        }

        if (emailExists(registerRequestDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new PlayerCreationResponseDTO(null, "Email already exists"));
        }

        Player newPlayer = createNewPlayer(registerRequestDTO);
        Profile profile = createNewProfile(registerRequestDTO);
        PlayerRatingHistory playerRatingHistory = createNewPlayerRatingHistory(profile);

        newPlayer.setProfile(profile);
        profile.setPlayer(newPlayer);
        playerRepository.save(newPlayer);
        playerRatingHistoryRepository.save(playerRatingHistory);

        return ResponseEntity.ok(new PlayerCreationResponseDTO(newPlayer.getPlayerId(), "Player and Profile created successfully"));
    }

    /**
     * Checks if a given username already exists in the system.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    private boolean usernameExists(String username) {
            return playerRepository.findByUsername(username).isPresent();
    }

    /**
     * Checks if a given email address is already used by a player.
     *
     * @param email The email address to check.
     * @return True if the email is already in use, false otherwise.
     */
    private boolean emailExists(String email) {
        return playerRepository.findByEmail(email).isPresent();
    }

    /**
     * Creates a new Player object from the provided registration details.
     *
     * @param registerRequestDTO The registration details.
     * @return A new Player object.
     */
    private Player createNewPlayer(RegisterRequestDTO registerRequestDTO) {
        Player player = new Player();
        player.setUsername(registerRequestDTO.getUsername());
        player.setPassword(registerRequestDTO.getPassword());
        player.setEmail(registerRequestDTO.getEmail());
        player.setJoinedDate(LocalDate.now());
        player.setTournamentCount(0);
        return player;
    }

    /**
     * Creates a new Profile object from the provided registration details.
     *
     * @param registerRequestDTO The registration details.
     * @return A new Profile object.
     */
    private Profile createNewProfile(RegisterRequestDTO registerRequestDTO) {
        Profile profile = new Profile();
        profile.setGender(registerRequestDTO.getGender());
        profile.setCountry(registerRequestDTO.getCountry());
        profile.setRegion(registerRequestDTO.getRegion());
        profile.setCity(registerRequestDTO.getCity());
        profile.setBirthDate(registerRequestDTO.getBirthDate());
        profile.setLastActive(LocalDate.now());
        return profile;
    }

    /**
     * Creates a new PlayerRatingHistory object for the given player profile.
     *
     * @param profile The profile of the player.
     * @return A new PlayerRatingHistory object.
     */
    private PlayerRatingHistory createNewPlayerRatingHistory(Profile profile) {
        PlayerRatingHistory playerRatingHistory = new PlayerRatingHistory();
        playerRatingHistory.setPlayer(profile.getPlayer());
        playerRatingHistory.setGlickoRating(profile.getGlickoRating());
        playerRatingHistory.setRatingDeviation(profile.getRatingDeviation());
        playerRatingHistory.setVolatility(profile.getVolatility());
        playerRatingHistory.setDate(LocalDateTime.now());
        return playerRatingHistory;
    }

    /**
     * Updates a player's password.
     *
     * @param playerId   The ID of the player whose password will be updated.
     * @param newPassword The new password to set.
     */
    @Override
    public void updatePlayerPassword(Long playerId, String newPassword) {
        Player player = getPlayerById(playerId);
        player.setPassword(newPassword);
        playerRepository.save(player);
    }

    /**
     * Retrieves player details by username and maps them to a UserDTO.
     *
     * @param username The username of the player.
     * @return A UserDTO containing the player's details.
     */
    @Override
    public UserDTO findPlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with username: " + username));
        return PlayerMapper.toUserDTO(player);
    }

    /**
     * Retrieves player details by player ID and maps them to a UserDTO.
     *
     * @param playerId The ID of the player.
     * @return A UserDTO containing the player's details.
     */
    @Override
    public UserDTO findPlayerById(Long playerId) {
        Player player = getPlayerById(playerId);
        return PlayerMapper.toUserDTO(player);
    }

    /**
     * Retrieves player details by email and maps them to a UserDTO.
     *
     * @param email The email address of the player.
     * @return A UserDTO containing the player's details.
     */
    @Override
    public UserDTO findPlayerByEmail(String email) {
        Player player = playerRepository.findByEmail(email)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with email: " + email));
        return PlayerMapper.toUserDTO(player);
    }

    /**
     * Retrieves detailed player information by player ID.
     *
     * @param playerId The ID of the player.
     * @return A PlayerDTO containing the detailed player information.
     */
    @Override
    public PlayerDTO getPlayerDetails(Long playerId) {
        Player player = getPlayerById(playerId);
        return PlayerMapper.toPlayerDTO(player);
    }

    /**
     * Reports a player for inappropriate behavior and sends an email report.
     *
     * @param reportRequestDTO The details of the player being reported.
     * @return A ResponseEntity indicating the result of the email sending operation.
     */
    @Override
    public ResponseEntity<String> reportPlayer(ReportPlayerRequestDTO reportRequestDTO) {
        ReportEmailDTO reportEmailDTO = createReportEmailDTO(
                reportRequestDTO.getReporterPlayerUsername(),
                reportRequestDTO.getReportedPlayerUsername(),
                reportRequestDTO.getReason(),
                reportRequestDTO.getDescription()
        );

        return sendReportEmail(reportEmailDTO);
    }

    /**
     * Creates a ReportEmailDTO containing the details of the reported player.
     *
     * @param reporterUsername  The username of the reporter.
     * @param reportedUsername The username of the reported player.
     * @param reason            The reason for the report.
     * @param description       The description of the incident.
     * @return A populated ReportEmailDTO.
     */
    private ReportEmailDTO createReportEmailDTO(String reporterUsername, String reportedUsername, String reason, String description) {
        return new ReportEmailDTO(reporterUsername, reportedUsername, reason, description);
    }

    /**
     * Sends a report email to the email service.
     *
     * @param reportEmailDTO The details of the report to send.
     * @return A ResponseEntity indicating the result of the email sending operation.
     */
    private ResponseEntity<String> sendReportEmail(ReportEmailDTO reportEmailDTO) {
        try {
            emailServiceClient.sendPlayerReportEmail(reportEmailDTO);
            return ResponseEntity.ok("Report email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send report email.");
        }
    }

    /**
     * Blacklists a player by setting their blacklisted status to true.
     *
     * @param playerId The ID of the player to blacklist.
     */
    @Override
    @Transactional
    public void blacklistPlayer(Long playerId) {
        Player player = getPlayerById(playerId);
        player.setBlacklisted(true);
        playerRepository.save(player);
    }

    /**
     * Whitelists a player by setting their blacklisted status to false.
     *
     * @param playerId The ID of the player to whitelist.
     */
    @Override
    @Transactional
    public void whitelistPlayer(Long playerId) {
        Player player = getPlayerById(playerId);
        player.setBlacklisted(false);
        playerRepository.save(player);
    }

    /**
     * Searches for players by their username, returning a paginated list of matching players.
     *
     * @param query The search query (username to search).
     * @param page  The page number to retrieve.
     * @param size  The number of players per page.
     * @return A paginated list of PlayerDTO objects matching the search query.
     */
    @Override
    public Page<PlayerDTO> searchPlayers(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return playerRepository.findByUsernameContainingIgnoreCase(query, pageable)
                .map(PlayerMapper::toPlayerDTO);
    }

    /**
     * Follows a player on behalf of the follower.
     *
     * @param followerId The ID of the follower player.
     * @param followedId The ID of the player to follow.
     * @return A ResponseEntity indicating the result of the follow operation.
     * @throws IllegalArgumentException if the follower attempts to follow themselves or if they are already following the player.
     */
    @Override
    public ResponseEntity<String> followPlayer(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }

        Player follower = getPlayerById(followerId);
        Player followed = getPlayerById(followedId);

        if (followRepository.findByFollowerAndFollowed(follower, followed).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already following this player.");
        }

        followRepository.save(new Follow(follower, followed));
        return ResponseEntity.ok("Successfully followed the player.");
    }

    /**
     * Unfollows a player on behalf of the follower.
     *
     * @param followerId The ID of the follower player.
     * @param followedId The ID of the player to unfollow.
     * @return A ResponseEntity indicating the result of the unfollow operation.
     * @throws IllegalArgumentException if the follower is not following the player.
     */
    @Override
    public ResponseEntity<String> unfollowPlayer(Long followerId, Long followedId) {
        Player follower = getPlayerById(followerId);
        Player followed = getPlayerById(followedId);
        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new IllegalArgumentException("Not following this player."));
        followRepository.delete(follow);
        return ResponseEntity.ok("Successfully unfollowed the player.");
    }

    /**
     * Retrieves a paginated list of players who are followed by the specified player.
     *
     * @param followerId The ID of the follower player.
     * @param page       The page number to retrieve.
     * @param size       The number of players per page.
     * @return A paginated list of PlayerDTO objects representing the players followed by the specified player.
     */
    @Override
    public Page<PlayerDTO> getFollowedPlayers(Long followerId, int page, int size) {
        Player follower = getPlayerById(followerId);
        Pageable pageable = PageRequest.of(page, size);
        return followRepository.findByFollower(follower, pageable).map(follow -> PlayerMapper.toPlayerDTO(follow.getFollowed()));
    }

    /**
     * Retrieves a paginated list of players who follow the specified player.
     *
     * @param playerId The ID of the player whose followers are to be retrieved.
     * @param page     The page number to retrieve.
     * @param size     The number of followers per page.
     * @return A paginated list of PlayerDTO objects representing the players who follow the specified player.
     */
    @Override
    public Page<PlayerDTO> getFollowers(Long playerId, int page, int size) {
        Player followed = getPlayerById(playerId);
        Pageable pageable = PageRequest.of(page, size);
        return followRepository.findByFollowed(followed, pageable).map(follow -> PlayerMapper.toPlayerDTO(follow.getFollower()));
    }

    /**
     * Retrieves the player by their ID, or throws an exception if not found.
     *
     * @param playerId The ID of the player.
     * @return The player with the specified ID.
     * @throws PlayerNotFoundException if the player is not found.
     */
    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
    }



}
