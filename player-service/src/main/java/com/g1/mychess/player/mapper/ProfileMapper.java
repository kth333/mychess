package com.g1.mychess.player.mapper;

import com.g1.mychess.player.dto.LeaderboardProfileDTO;
import com.g1.mychess.player.model.Profile;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileMapper {

    // Mapper function to convert List of Profiles to List of LeaderboardProfileDTO
    public static List<LeaderboardProfileDTO> toLeaderboardProfileDTOList(List<Profile> profiles) {
        return profiles.stream()
                .map(profile -> new LeaderboardProfileDTO(
                        profile.getPlayerId(),
                        profile.getPlayer() != null ? profile.getPlayer().getUsername() : "",
                        profile.getRank(),
                        profile.getCountry(),
                        profile.getGlickoRating()))
                .collect(Collectors.toList());
    }
}
