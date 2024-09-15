package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.PlayerDTO;
import com.g1.mychess.player.dto.RegisterRequestDTO;
import com.g1.mychess.player.dto.PlayerCreationResponseDTO;
import com.g1.mychess.player.dto.UserDTO;
import com.g1.mychess.player.exception.RatingNotFoundException;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.g1.mychess.player.exception.PlayerNotFoundException;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;

    public PlayerService(PlayerRepository playerRepository, PlayerRatingHistoryRepository playerRatingHistoryRepository) {
        this.playerRepository = playerRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
    }

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

        return ResponseEntity.ok(new PlayerCreationResponseDTO(newPlayer.getPlayerId(), "Player created successfully"));
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

        PlayerRatingHistory latestRating = playerRatingHistoryRepository.findLatestRatingByPlayerId(playerId)
                .orElseThrow(() -> new RatingNotFoundException("Rating history not found for player with id: " + playerId));

        return new PlayerDTO(
                player.getPlayerId(),
                player.getUsername(),
                player.getProfile().getAge(),
                player.getProfile().getGender(),
                latestRating.getGlickoRating(),
                latestRating.getRatingDeviation(),
                latestRating.getVolatility()
        );
    }
}