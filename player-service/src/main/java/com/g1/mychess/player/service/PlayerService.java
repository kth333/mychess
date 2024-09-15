package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.PlayerDTO;
import com.g1.mychess.player.dto.RegisterRequestDTO;
import com.g1.mychess.player.dto.PlayerCreationResponseDTO;
import com.g1.mychess.player.dto.UserDTO;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.g1.mychess.player.exception.PlayerNotFoundException;

import java.time.LocalDateTime;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;

    private final ProfileRepository profileRepository;

    public PlayerService(PlayerRepository playerRepository, PlayerRatingHistoryRepository playerRatingHistoryRepository, ProfileRepository profileRepository) {
        this.playerRepository = playerRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
        this.profileRepository = profileRepository;
    }

    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO registerRequestDTO) {
        // Check if the username already exists
        if (playerRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new PlayerCreationResponseDTO(null, "Username already exists"));
        }

        // Check if the email already exists
        if (playerRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new PlayerCreationResponseDTO(null, "Email already exists"));
        }

        Player newPlayer = new Player();
        newPlayer.setUsername(registerRequestDTO.getUsername());
        newPlayer.setPassword(registerRequestDTO.getPassword());
        newPlayer.setEmail(registerRequestDTO.getEmail());

        // Save the player to get the ID assigned in profile
        playerRepository.save(newPlayer);

        Profile profile = new Profile();
        profile.setPlayer(newPlayer);
        profile.setGender(registerRequestDTO.getGender());
        profile.setCountry(registerRequestDTO.getCountry());
        profile.setRegion(registerRequestDTO.getRegion());
        profile.setCity(registerRequestDTO.getCity());
        profile.setBirthDate(registerRequestDTO.getBirthDate());

        profileRepository.save(profile);

        // Update the player with the profile and save again
        newPlayer.setProfile(profile);
        playerRepository.save(newPlayer);

        return ResponseEntity.ok(new PlayerCreationResponseDTO(newPlayer.getPlayerId(), "Player and Profile created successfully"));
    }

    public void updatePlayerPassword(Long playerId, String newPassword) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with ID " + playerId + " not found."));

        player.setPassword(newPassword);

        playerRepository.save(player);
    }

    public UserDTO findPlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with username: " + username));

        return new UserDTO(player.getPlayerId(), player.getUsername(), player.getPassword(), player.getEmail(), player.getRole());
    }

    public UserDTO findPlayerById(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with player ID: " + playerId));

        return new UserDTO(player.getPlayerId(), player.getUsername(), player.getPassword(), player.getEmail(), player.getRole());
    }

    public UserDTO findPlayerByEmail(String email) {
        Player player = playerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with email: " + email));

        return new UserDTO(player.getPlayerId(), player.getUsername(), player.getPassword(), player.getEmail(), player.getRole());
    }

    public PlayerDTO getPlayerWithRatingDetails(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));

        Profile profile = player.getProfile();

        return new PlayerDTO(
                player.getPlayerId(),
                player.getUsername(),
                profile.getAge(),
                profile.getGender(),
                profile.getGlickoRating(),
                profile.getRatingDeviation(),
                profile.getVolatility()
        );
    }

    public void updatePlayerRating(Long playerId, int glickoRating, double ratingDeviation, double volatility) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));

        // Update profile with latest rating
        Profile profile = player.getProfile();
        profile.setGlickoRating(glickoRating);
        profile.setRatingDeviation(ratingDeviation);
        profile.setVolatility(volatility);

        // Optionally, also save this in the rating history table
        PlayerRatingHistory ratingHistory = new PlayerRatingHistory(player, glickoRating, ratingDeviation, volatility, LocalDateTime.now());
        playerRatingHistoryRepository.save(ratingHistory);

        // Save the updated profile
        profileRepository.save(profile);
    }
}