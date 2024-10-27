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
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final ProfileRepository profileRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            ProfileRepository profileRepository,
            PlayerRatingHistoryRepository playerRatingHistoryRepository
    ) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
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
        Profile profile = createNewProfile(newPlayer, registerRequestDTO);

        newPlayer.setProfile(profile);
        playerRepository.save(newPlayer);

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
        playerRepository.save(player);
        return player;
    }

    private Profile createNewProfile(Player player, RegisterRequestDTO registerRequestDTO) {
        Profile profile = new Profile();
        profile.setPlayer(player);
        profile.setGender(registerRequestDTO.getGender());
        profile.setCountry(registerRequestDTO.getCountry());
        profile.setRegion(registerRequestDTO.getRegion());
        profile.setCity(registerRequestDTO.getCity());
        profile.setBirthDate(registerRequestDTO.getBirthDate());
        profileRepository.save(profile);
        return profile;
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
    public PlayerDTO getPlayerWithRatingDetails(Long playerId) {
        Player player = getPlayerById(playerId);
        return PlayerMapper.toPlayerDTO(player);
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
    public void updatePlayerRating(Long playerId, double glickoRating, double ratingDeviation, double volatility) {
        Player player = getPlayerById(playerId);
        Profile profile = player.getProfile();
        if (profile == null) {
            throw new PlayerNotFoundException("Profile not found for player ID: " + playerId);
        }

        updateProfileRating(profile, glickoRating, ratingDeviation, volatility);
        saveRatingHistory(player, glickoRating, ratingDeviation, volatility);
    }

    private void updateProfileRating(Profile profile, double glickoRating, double ratingDeviation, double volatility) {
        profile.setGlickoRating(glickoRating);
        profile.setRatingDeviation(ratingDeviation);
        profile.setVolatility(volatility);
        profileRepository.save(profile);
    }

    private void saveRatingHistory(Player player, double glickoRating, double ratingDeviation, double volatility) {
        PlayerRatingHistory ratingHistory = new PlayerRatingHistory(player, glickoRating, ratingDeviation, volatility, LocalDateTime.now());
        playerRatingHistoryRepository.save(ratingHistory);
    }

    @Override
    public AdminPlayerDTO getPlayerDetailsForAdmin(Long playerId) {
        Player player = getPlayerById(playerId);
        return PlayerMapper.toAdminPlayerDTO(player);
    }

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
    }
}