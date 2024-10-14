package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import com.g1.mychess.player.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;
    private final ProfileRepository profileRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerRatingHistoryRepository playerRatingHistoryRepository, ProfileRepository profileRepository) {
        this.playerRepository = playerRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO registerRequestDTO) {
        if (playerRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new PlayerCreationResponseDTO(null, "Username already exists"));
        }

        if (playerRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new PlayerCreationResponseDTO(null, "Email already exists"));
        }

        Player newPlayer = new Player();
        newPlayer.setUsername(registerRequestDTO.getUsername());
        newPlayer.setPassword(registerRequestDTO.getPassword());
        newPlayer.setEmail(registerRequestDTO.getEmail());

        playerRepository.save(newPlayer);

        Profile profile = new Profile();
        profile.setPlayer(newPlayer);
        profile.setGender(registerRequestDTO.getGender());
        profile.setCountry(registerRequestDTO.getCountry());
        profile.setRegion(registerRequestDTO.getRegion());
        profile.setCity(registerRequestDTO.getCity());
        profile.setBirthDate(registerRequestDTO.getBirthDate());

        profileRepository.save(profile);

        newPlayer.setProfile(profile);
        playerRepository.save(newPlayer);

        return ResponseEntity.ok(new PlayerCreationResponseDTO(newPlayer.getPlayerId(), "Player and Profile created successfully"));
    }

    @Override
    public void updatePlayerPassword(Long playerId, String newPassword) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with ID " + playerId + " not found."));
        player.setPassword(newPassword);
        playerRepository.save(player);
    }

    @Override
    public UserDTO findPlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with username: " + username));
        return new UserDTO(player.getPlayerId(), player.getUsername(), player.getPassword(), player.getEmail(), player.getRole());
    }

    @Override
    public UserDTO findPlayerById(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with player ID: " + playerId));
        return new UserDTO(player.getPlayerId(), player.getUsername(), player.getPassword(), player.getEmail(), player.getRole());
    }

    @Override
    public UserDTO findPlayerByEmail(String email) {
        Player player = playerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with email: " + email));
        return new UserDTO(player.getPlayerId(), player.getUsername(), player.getPassword(), player.getEmail(), player.getRole());
    }

    @Override
    public PlayerDTO getPlayerWithRatingDetails(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
        Profile profile = player.getProfile();
        return new PlayerDTO(
                player.getPlayerId(),
                player.isBlacklisted(),
                player.getUsername(),
                profile.getAge(),
                profile.getGender(),
                profile.getGlickoRating(),
                profile.getRatingDeviation(),
                profile.getVolatility()
        );
    }

    @Override
    @Transactional
    public void blacklistPlayer(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
        player.setBlacklisted(true);
        playerRepository.save(player);
    }

    @Override
    @Transactional
    public void whitelistPlayer(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
        player.setBlacklisted(false);
        playerRepository.save(player);
    }

    @Override
    public void updatePlayerRating(Long playerId, double glickoRating, double ratingDeviation, double volatility) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
        Profile profile = player.getProfile();
        profile.setGlickoRating(glickoRating);
        profile.setRatingDeviation(ratingDeviation);
        profile.setVolatility(volatility);

        PlayerRatingHistory ratingHistory = new PlayerRatingHistory(player, glickoRating, ratingDeviation, volatility, LocalDateTime.now());
        playerRatingHistoryRepository.save(ratingHistory);

        profileRepository.save(profile);
    }

    @Override
    public AdminPlayerDTO getPlayerDetailsForAdmin(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
        return new AdminPlayerDTO(
                player.getPlayerId(),
                player.isBlacklisted(),
                player.getUsername(),
                player.getEmail()
        );
    }
}