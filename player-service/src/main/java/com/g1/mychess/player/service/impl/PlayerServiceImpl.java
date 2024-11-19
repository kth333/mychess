package com.g1.mychess.player.service.impl;

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
import com.g1.mychess.player.repository.ProfileRepository;
import com.g1.mychess.player.service.PlayerService;
import com.g1.mychess.player.client.EmailServiceClient;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final ProfileRepository profileRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;
    private final EmailServiceClient emailServiceClient;
    private final FollowRepository followRepository;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            ProfileRepository profileRepository,
            PlayerRatingHistoryRepository playerRatingHistoryRepository,
            EmailServiceClient emailServiceClient,
            FollowRepository followRepository
    ) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
        this.emailServiceClient = emailServiceClient;
        this.followRepository = followRepository;
    }

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

        newPlayer.setProfile(profile);
        profile.setPlayer(newPlayer);
        PlayerRatingHistory playerRatingHistory = createNewPlayerRatingHistory(profile);

        playerRepository.save(newPlayer);
        playerRatingHistoryRepository.save(playerRatingHistory);

        return ResponseEntity.ok(new PlayerCreationResponseDTO(newPlayer.getPlayerId(), "Player and Profile created successfully"));
    }

    private boolean usernameExists(String username) {
        return playerRepository.findByUsername(username).isPresent();
    }

    private boolean emailExists(String email) {
        return playerRepository.findByEmail(email).isPresent();
    }

    private Player createNewPlayer(RegisterRequestDTO registerRequestDTO) {
        Player player = new Player();
        player.setUsername(registerRequestDTO.getUsername());
        player.setPassword(registerRequestDTO.getPassword());
        player.setEmail(registerRequestDTO.getEmail());
        player.setJoinedDate(LocalDate.now());
        player.setTournamentCount(0);
        return player;
    }

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

    private PlayerRatingHistory createNewPlayerRatingHistory(Profile profile) {
        PlayerRatingHistory playerRatingHistory = new PlayerRatingHistory();
        playerRatingHistory.setPlayer(profile.getPlayer());
        playerRatingHistory.setGlickoRating(profile.getGlickoRating());
        playerRatingHistory.setRatingDeviation(profile.getRatingDeviation());
        playerRatingHistory.setVolatility(profile.getVolatility());
        playerRatingHistory.setDate(LocalDateTime.now());
        return playerRatingHistory;
    }

    @Override
    public void updatePlayerPassword(Long playerId, String newPassword) {
        Player player = getPlayerById(playerId);
        player.setPassword(newPassword);
        playerRepository.save(player);
    }

    @Override
    public UserDTO findPlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with username: " + username));
        return PlayerMapper.toUserDTO(player);
    }

    @Override
    public UserDTO findPlayerById(Long playerId) {
        Player player = getPlayerById(playerId);
        return PlayerMapper.toUserDTO(player);
    }

    @Override
    public UserDTO findPlayerByEmail(String email) {
        Player player = playerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with email: " + email));
        return PlayerMapper.toUserDTO(player);
    }

    @Override
    public PlayerDTO getPlayerDetails(Long playerId) {
        Player player = getPlayerById(playerId);
        return PlayerMapper.toPlayerDTO(player);
    }

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

    private ReportEmailDTO createReportEmailDTO(String reporterUsername, String reportedUsername, String reason, String description) {
        return new ReportEmailDTO(reporterUsername, reportedUsername, reason, description);
    }

    private ResponseEntity<String> sendReportEmail(ReportEmailDTO reportEmailDTO) {
        try {
            emailServiceClient.sendPlayerReportEmail(reportEmailDTO);
            return ResponseEntity.ok("Report email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send report email.");
        }
    }

    @Override
    @Transactional
    public void blacklistPlayer(Long playerId) {
        Player player = getPlayerById(playerId);
        player.setBlacklisted(true);
        playerRepository.save(player);
    }

    @Override
    @Transactional
    public void whitelistPlayer(Long playerId) {
        Player player = getPlayerById(playerId);
        player.setBlacklisted(false);
        playerRepository.save(player);
    }

    @Override
    public Page<PlayerDTO> searchPlayers(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return playerRepository.findByUsernameContainingIgnoreCase(query, pageable)
                .map(PlayerMapper::toPlayerDTO);
    }

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

    @Override
    public ResponseEntity<String> unfollowPlayer(Long followerId, Long followedId) {
        Player follower = getPlayerById(followerId);
        Player followed = getPlayerById(followedId);
        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new IllegalArgumentException("Not following this player."));
        followRepository.delete(follow);
        return ResponseEntity.ok("Successfully unfollowed the player.");
    }

    @Override
    public Page<PlayerDTO> getFollowedPlayers(Long followerId, int page, int size) {
        Player follower = getPlayerById(followerId);
        Pageable pageable = PageRequest.of(page, size);
        return followRepository.findByFollower(follower, pageable).map(follow -> PlayerMapper.toPlayerDTO(follow.getFollowed()));
    }

    @Override
    public Page<PlayerDTO> getFollowers(Long playerId, int page, int size) {
        Player followed = getPlayerById(playerId);
        Pageable pageable = PageRequest.of(page, size);
        return followRepository.findByFollowed(followed, pageable).map(follow -> PlayerMapper.toPlayerDTO(follow.getFollower()));
    }

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
    }
}