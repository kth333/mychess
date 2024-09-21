package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final PlayerRepository playerRepository;

    public ProfileService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
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
