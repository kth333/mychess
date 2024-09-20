package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.PublicPlayerProfileDTO;
import com.g1.mychess.player.dto.PrivatePlayerProfileDTO;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Object getPlayerProfile(Long playerId) {
        // Retrieve the profile from the repository
        Profile profile = profileRepository.findByPlayer_PlayerId(playerId)
            .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Decide whether to return Public or Private DTO
        if (profile.isPublic()) {
            // Return PublicPlayerProfileDTO with full information
            return new PublicPlayerProfileDTO(
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
        } else {
            // Return PrivatePlayerProfileDTO 
            return new PrivatePlayerProfileDTO(
                profile.getPlayerId(),
                player.getUsername(),  
                player.getEmail()                        );
        }
    }
}
