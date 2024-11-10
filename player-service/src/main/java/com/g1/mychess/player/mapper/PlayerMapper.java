package com.g1.mychess.player.mapper;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.model.Player;
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
                profile != null ? profile.getAge() : null,
                profile != null ? profile.getGender() : null,
                profile != null ? profile.getGlickoRating() : null,
                profile != null ? profile.getRatingDeviation() : null,
                profile != null ? profile.getVolatility() : null
        );
    }

    public static AdminPlayerDTO toAdminPlayerDTO(Player player) {
        return new AdminPlayerDTO(
                player.getPlayerId(),
                player.isBlacklisted(),
                player.getUsername(),
                player.getEmail()
        );
    }

    public static PlayerProfileDTO toPlayerProfileDTO(Profile profile) {
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
}