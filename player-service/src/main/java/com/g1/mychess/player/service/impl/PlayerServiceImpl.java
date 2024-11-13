package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.mapper.PlayerMapper;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.model.Profile;
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

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final ProfileRepository profileRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;
    private final EmailServiceClient emailServiceClient;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            ProfileRepository profileRepository,
            PlayerRatingHistoryRepository playerRatingHistoryRepository,
            EmailServiceClient emailServiceClient
    ) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
        this.emailServiceClient = emailServiceClient;
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
        UserDTO reporter = findPlayerById(reportRequestDTO.getReporterPlayerId());
        UserDTO reportedPlayer = findPlayerById(reportRequestDTO.getReportedPlayerId());

        ReportEmailDTO reportEmailDTO = createReportEmailDTO(
                reporter.getUsername(),
                reportedPlayer.getUsername(),
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

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
    }
}