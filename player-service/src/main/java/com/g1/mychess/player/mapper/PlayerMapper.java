package com.g1.mychess.player.mapper;

import java.util.List;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.model.Profile;

/**
 * Mapper class for converting Player, Profile, and PlayerRatingHistory objects
 * into their corresponding Data Transfer Objects (DTOs).
 * This class contains methods for mapping entities to DTOs and vice versa.
 */
public class PlayerMapper {

    /**
     * Converts a Player object to a UserDTO object.
     *
     * @param player the Player object to convert.
     * @return a UserDTO object containing the player's information.
     */
    public static UserDTO toUserDTO(Player player) {
        return new UserDTO(
                player.getPlayerId(),
                player.getUsername(),
                player.getPassword(),
                player.getEmail(),
                player.getRole()
        );
    }

    /**
     * Converts a Player object to a PlayerDTO object.
     *
     * @param player the Player object to convert.
     * @return a PlayerDTO object containing the player's details and profile information.
     */
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

    /**
     * Converts a Profile object to a PlayerProfileDTO object.
     *
     * @param profile the Profile object to convert.
     * @return a PlayerProfileDTO object containing the profile's details.
     */
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

    /**
     * Updates the Profile object with data from the PlayerProfileUpdateDTO.
     *
     * @param profile the Profile object to update.
     * @param dto the PlayerProfileUpdateDTO containing the updated information.
     * @return the updated Profile object.
     */
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

    /**
     * Converts a PlayerRatingHistory object to a PlayerRatingHistoryDTO object.
     *
     * @param playerRatingHistory the PlayerRatingHistory object to convert.
     * @return a PlayerRatingHistoryDTO object containing the rating history data.
     */
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

    /**
     * Converts a List of PlayerRatingHistory objects to a List of PlayerRatingHistoryDTO objects.
     *
     * @param playerRatingHistoryList the List of PlayerRatingHistory objects to convert.
     * @return a List of PlayerRatingHistoryDTO objects.
     */
    public static List<PlayerRatingHistoryDTO> toPlayerRatingHistoryDTOList(List<PlayerRatingHistory> playerRatingHistoryList){
        return playerRatingHistoryList.stream().map(PlayerMapper::toPlayerRatingHistoryDTO).toList();
    }

    /**
     * Extracts the username from a Profile object.
     *
     * @param profile the Profile object from which to extract the username.
     * @return the username of the associated Player, or null if no player exists.
     */
    private static String extractUsernameFromProfile(Profile profile) {
        Player player = profile.getPlayer();
        return player != null ? player.getUsername() : null;
    }
}