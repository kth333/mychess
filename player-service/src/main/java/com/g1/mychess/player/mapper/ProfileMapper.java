package com.g1.mychess.player.mapper;

import com.g1.mychess.player.dto.LeaderboardProfileDTO;
import com.g1.mychess.player.model.Profile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting Profile objects to LeaderboardProfileDTO objects.
 * This class provides methods to transform a list of Profiles into a list of LeaderboardProfileDTOs.
 */
public class ProfileMapper {

    /**
     * Converts a List of Profile objects to a List of LeaderboardProfileDTO objects.
     * The conversion includes mapping key fields from the Profile model to the LeaderboardProfileDTO.
     *
     * @param profiles the list of Profile objects to convert.
     * @return a list of LeaderboardProfileDTO objects representing the leaderboard data.
     */
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