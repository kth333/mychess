package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileUpdateDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.mapper.PlayerMapper;
import com.g1.mychess.player.model.Profile;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.repository.ProfileRepository;
import com.g1.mychess.player.service.ProfileService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final PlayerRepository playerRepository;
    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(
            PlayerRepository playerRepository,
            ProfileRepository profileRepository
    ) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public void updateProfileRating(PlayerRatingUpdateDTO ratingUpdateDTO) {
        Profile profile = getProfileByPlayerId(ratingUpdateDTO.getPlayerId());
        updateProfileRating(profile, ratingUpdateDTO);
    }

    private void updateProfileRating(Profile profile, PlayerRatingUpdateDTO ratingUpdateDTO) {
        profile.setGlickoRating(ratingUpdateDTO.getGlickoRating());
        profile.setRatingDeviation(ratingUpdateDTO.getRatingDeviation());
        profile.setVolatility(ratingUpdateDTO.getVolatility());
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

    @Override
    @Transactional
    public ResponseEntity<String> updatePlayerProfile(Long playerId, PlayerProfileUpdateDTO profileUpdateDTO) {
        Profile profile = profileRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for player ID: " + playerId));

        profile.setFullName(profileUpdateDTO.getFullName());
        profile.setBio(profileUpdateDTO.getBio());
        profile.setAvatarUrl(profileUpdateDTO.getAvatarUrl());
        profile.setCountry(profileUpdateDTO.getCountry());
        profile.setRegion(profileUpdateDTO.getRegion());
        profile.setCity(profileUpdateDTO.getCity());
        profile.setPublic(profileUpdateDTO.isPublic());

        profileRepository.save(profile);

        return ResponseEntity.ok("Profile updated successfully");
    }

    

}