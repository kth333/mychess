package com.g1.mychess.player.mapper;

import java.util.List;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.model.Profile;

public class PlayerMapper {

    public static UserDTO toUserDTO(Player player) {
        return new UserDTO(
                player.getPlayerId(),
                player.getUsername(),
                player.getPassword(),
                player.getEmail(),
                player.getRole()
        );
    }

    public static PlayerDTO toPlayerDTO(Player player) {
        Profile profile = player.getProfile();
        return new PlayerDTO(
                player.getPlayerId(),
                player.isBlacklisted(),
                player.getUsername(),
                player.getEmail(),
                profile != null ? profile.getAge() : null,
                profile != null ? profile.getGender() : null,
                profile != null ? profile.getGlickoRating() : null,
                profile != null ? profile.getRatingDeviation() : null,
                profile != null ? profile.getVolatility() : null
        );
    }

    public static PlayerProfileDTO toPlayerProfileDTO(Profile profile) {
        return new PlayerProfileDTO(
                profile.getPlayerId(),
                extractUsernameFromProfile(profile),
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

    public static Profile updateProfileFromDTO(Profile profile, PlayerProfileUpdateDTO dto) {
        profile.setFullName(dto.getFullName());
        profile.setBio(dto.getBio());
        profile.setAvatarUrl(dto.getAvatarUrl());
        profile.setCountry(dto.getCountry());
        profile.setRegion(dto.getRegion());
        profile.setCity(dto.getCity());
        profile.setPublic(dto.isPublic());
        return profile;
    }

    public static PlayerRatingHistoryDTO toPlayerRatingHistoryDTO(PlayerRatingHistory playerRatingHistory) {
        return new PlayerRatingHistoryDTO(
                playerRatingHistory.getId(),
                playerRatingHistory.getPlayer().getPlayerId(),
                playerRatingHistory.getGlickoRating(),
                playerRatingHistory.getRatingDeviation(),
                playerRatingHistory.getVolatility(),
                playerRatingHistory.getDate()
        );
    }
    
    public static List<PlayerRatingHistoryDTO> toPlayerRatingHistoryDTOList(List<PlayerRatingHistory> playerRatingHistoryList){
        return playerRatingHistoryList.stream().map(PlayerMapper::toPlayerRatingHistoryDTO).toList();
    }

    private static String extractUsernameFromProfile(Profile profile) {
        Player player = profile.getPlayer();
        System.out.println(player.getUsername());
        System.out.println(player);
        return player != null ? player.getUsername() : null;
    }
}