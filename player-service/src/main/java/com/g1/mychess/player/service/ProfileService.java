package com.g1.mychess.player.service;

import org.springframework.http.ResponseEntity;

import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.dto.PlayerProfileUpdateDTO;

public interface ProfileService {

    void updateProfileRating(PlayerRatingUpdateDTO ratingUpdateDTO);

    PlayerProfileDTO getPlayerProfile(Long playerId);

    ResponseEntity<String> updatePlayerProfile(Long playerId, PlayerProfileUpdateDTO profileUpdateDTO);
}