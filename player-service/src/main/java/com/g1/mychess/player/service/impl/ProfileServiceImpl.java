package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import com.g1.mychess.player.service.ProfileService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final PlayerRepository playerRepository;
    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(PlayerRepository playerRepository, ProfileRepository profileRepository) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public void updatePlayerProfile(PlayerRatingUpdateDTO ratingUpdateDTO) {
        Profile profile = profileRepository.findById(ratingUpdateDTO.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for player ID: " + ratingUpdateDTO.getPlayerId()));

        profile.setGlickoRating(ratingUpdateDTO.getGlickoRating());
        profile.setRatingDeviation(ratingUpdateDTO.getRatingDeviation());
        profile.setVolatility(ratingUpdateDTO.getVolatility());

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
    }

    @Override
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