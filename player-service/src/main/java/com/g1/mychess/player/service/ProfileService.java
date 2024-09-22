package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final PlayerRepository playerRepository;

    private final ProfileRepository profileRepository;

    public ProfileService(PlayerRepository playerRepository, ProfileRepository profileRepository) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public void updatePlayerProfile(PlayerRatingUpdateDTO ratingUpdateDTO) {
        Profile profile = profileRepository.findById(ratingUpdateDTO.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for player ID: " + ratingUpdateDTO.getPlayerId()));

        // Update Profile with new rating information
        profile.setGlickoRating(ratingUpdateDTO.getGlickoRating());
        profile.setRatingDeviation(ratingUpdateDTO.getRatingDeviation());
        profile.setVolatility(ratingUpdateDTO.getVolatility());

        // Update win/loss/draw counts based on match result
        updateWinLossDrawCounts(profile, ratingUpdateDTO);

        profileRepository.save(profile);
    }

    private void updateWinLossDrawCounts(Profile profile, PlayerRatingUpdateDTO ratingUpdateDTO) {
        switch (ratingUpdateDTO.getResult()) {
            case "WIN":
                profile.setTotalWins(profile.getTotalWins() + 1);
                break;
            case "LOSS":
                profile.setTotalLosses(profile.getTotalLosses() + 1);
                break;
            case "DRAW":
                profile.setTotalDraws(profile.getTotalDraws() + 1);
                break;
        }
        // Additional logic to track by time control can be added here if needed
    }

    public PlayerProfileDTO getPlayerProfile(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));

        Profile profile = player.getProfile();

        return new PlayerProfileDTO(
                profile.getPlayerId(),
                profile.getFullName(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getGender(),
                profile.getCountry(),
                profile.getRegion(),
                profile.getCity(),
                profile.getBirthDate(),
                profile.getRank(),
                profile.getGlickoRating(),
                profile.getRatingDeviation(),
                profile.getVolatility(),
                profile.getTotalWins(),
                profile.getTotalLosses(),
                profile.getTotalDraws(),
                profile.isPublic(),
                profile.getAge()
        );
    }
}
