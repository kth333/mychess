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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final PlayerRepository playerRepository;
    private final ProfileRepository profileRepository;

    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;

    public ProfileServiceImpl(
            PlayerRepository playerRepository,
            ProfileRepository profileRepository, PlayerRatingHistoryRepository playerRatingHistoryRepository
    ) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
    }

    @Override
    @Transactional
    public void updateProfileRating(PlayerRatingUpdateDTO ratingUpdateDTO) {
        Profile profile = getProfileByPlayerId(ratingUpdateDTO.getPlayerId());
        Player player = playerRepository.getReferenceById(ratingUpdateDTO.getPlayerId());
        double glickoRating = ratingUpdateDTO.getGlickoRating();
        double ratingDeviation = ratingUpdateDTO.getRatingDeviation();
        double volatility = ratingUpdateDTO.getVolatility();
        saveRatingHistory(player, glickoRating, ratingDeviation, volatility);
        updateProfileRating(profile, ratingUpdateDTO);
    }

    private void updateProfileRating(Profile profile, PlayerRatingUpdateDTO ratingUpdateDTO) {
        profile.setGlickoRating(ratingUpdateDTO.getGlickoRating());
        profile.setRatingDeviation(ratingUpdateDTO.getRatingDeviation());
        profile.setVolatility(ratingUpdateDTO.getVolatility());
        CustomChessRank rank = CustomChessRank.getRankForRating(ratingUpdateDTO.getGlickoRating());
        profile.setRank(rank);
        profileRepository.save(profile);
    }

    @Override
    public PlayerProfileDTO getPlayerProfile(Long playerId) {
        Profile profile = getProfileByPlayerId(playerId);
        return PlayerMapper.toPlayerProfileDTO(profile);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updatePlayerProfile(Long playerId, PlayerProfileUpdateDTO profileUpdateDTO) {
        Profile profile = getProfileByPlayerId(playerId);
        PlayerMapper.updateProfileFromDTO(profile, profileUpdateDTO);
        profileRepository.save(profile);
        return ResponseEntity.ok("Profile updated successfully");
    }

    private Profile getProfileByPlayerId(Long playerId) {
        return profileRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
    }

    private void saveRatingHistory(Player player, double glickoRating, double ratingDeviation, double volatility) {
        PlayerRatingHistory ratingHistory = new PlayerRatingHistory(player, glickoRating, ratingDeviation, volatility, LocalDateTime.now());
        playerRatingHistoryRepository.save(ratingHistory);
    }

    @Override
    @Transactional
    public List<LeaderboardProfileDTO> getLeaderboard() {
        return ProfileMapper.toLeaderboardProfileDTOList(profileRepository.findTop50ByOrderByGlickoRatingDesc());
                
    }

}