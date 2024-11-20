package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.dto.LeaderboardProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileUpdateDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.mapper.PlayerMapper;
import com.g1.mychess.player.mapper.ProfileMapper;
import com.g1.mychess.player.model.CustomChessRank;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import com.g1.mychess.player.service.ProfileService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing player profiles.
 * Provides functionality for updating player ratings, retrieving player profiles,
 * updating player profile information, and fetching the leaderboard.
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    private PlayerRepository playerRepository;
    private ProfileRepository profileRepository;
    private PlayerRatingHistoryRepository playerRatingHistoryRepository;

    /**
     * Constructor for ProfileServiceImpl.
     *
     * @param playerRepository Repository for player data
     * @param profileRepository Repository for player profile data
     * @param playerRatingHistoryRepository Repository for player rating history data
     */
    public ProfileServiceImpl(
            PlayerRepository playerRepository,
            ProfileRepository profileRepository, PlayerRatingHistoryRepository playerRatingHistoryRepository
    ) {
        this.playerRepository = playerRepository;
    }

    @Autowired
    public void setProfileRepository(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
    }

    /**
     * Updates the player's rating and stores the rating history.
     *
     * @param ratingUpdateDTO DTO containing the updated rating information for the player
     * @throws PlayerNotFoundException if the player with the specified ID is not found
     */
    @Override
    @Transactional
    public void updateProfileRating(PlayerRatingUpdateDTO ratingUpdateDTO) {
        double glickoRating = ratingUpdateDTO.getGlickoRating();
        double ratingDeviation = ratingUpdateDTO.getRatingDeviation();
        double volatility = ratingUpdateDTO.getVolatility();
        Profile profile = getProfileByPlayerId(ratingUpdateDTO.getPlayerId());
        saveRatingHistory(profile.getPlayer(), glickoRating, ratingDeviation, volatility);

        updateProfileRating(profile, ratingUpdateDTO);
    }

    /**
     * Updates the player's profile with new rating information and recalculates their rank.
     *
     * @param profile The player's profile to update
     * @param ratingUpdateDTO DTO containing the updated rating information for the player
     */
    private void updateProfileRating(Profile profile, PlayerRatingUpdateDTO ratingUpdateDTO) {
        profile.setGlickoRating(ratingUpdateDTO.getGlickoRating());
        profile.setRatingDeviation(ratingUpdateDTO.getRatingDeviation());
        profile.setVolatility(ratingUpdateDTO.getVolatility());
        profile.setRank(CustomChessRank.getRankForRating(ratingUpdateDTO.getGlickoRating()));
        profileRepository.save(profile);
    }

    /**
     * Retrieves the profile for a given player by their ID.
     *
     * @param playerId The ID of the player
     * @return PlayerProfileDTO containing the player's profile information
     * @throws PlayerNotFoundException if the player with the specified ID is not found
     */
    @Override
    public PlayerProfileDTO getPlayerProfile(Long playerId) {
        Profile profile = getProfileByPlayerId(playerId);
        return PlayerMapper.toPlayerProfileDTO(profile);
    }

    /**
     * Updates the player's profile with the provided details.
     *
     * @param playerId The ID of the player
     * @param profileUpdateDTO DTO containing the new profile information
     * @return ResponseEntity with a success message
     * @throws PlayerNotFoundException if the player with the specified ID is not found
     */
    @Override
    @Transactional
    public ResponseEntity<String> updatePlayerProfile(Long playerId, PlayerProfileUpdateDTO profileUpdateDTO) {
        Profile profile = getProfileByPlayerId(playerId);
        PlayerMapper.updateProfileFromDTO(profile, profileUpdateDTO);
        profileRepository.save(profile);
        return ResponseEntity.ok("Profile updated successfully");
    }

    /**
     * Retrieves a player profile by their ID.
     *
     * @param playerId The ID of the player
     * @return The player's profile
     * @throws PlayerNotFoundException if the player is not found in the repository
     */
    private Profile getProfileByPlayerId(Long playerId) {
        return profileRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
    }

    /**
     * Saves a new entry in the player's rating history.
     *
     * @param player The player whose rating history is being saved
     * @param glickoRating The Glicko rating of the player
     * @param ratingDeviation The rating deviation of the player
     * @param volatility The volatility of the player's rating
     */
    private void saveRatingHistory(Player player, double glickoRating, double ratingDeviation, double volatility) {
        PlayerRatingHistory ratingHistory = new PlayerRatingHistory(player, glickoRating, ratingDeviation, volatility, LocalDateTime.now());
        playerRatingHistoryRepository.save(ratingHistory);
    }

    /**
     * Retrieves the leaderboard containing the top 50 players based on their Glicko rating.
     *
     * @return List of LeaderboardProfileDTOs containing the top 50 player profiles ordered by Glicko rating
     */
    @Override
    @Transactional
    public List<LeaderboardProfileDTO> getLeaderboard() {
        return ProfileMapper.toLeaderboardProfileDTOList(profileRepository.findTop50ByOrderByGlickoRatingDesc());
                
    }

}