package com.g1.mychess.player.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.g1.mychess.player.dto.LeaderboardProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.dto.PlayerProfileUpdateDTO;

public interface ProfileService {

    void updateProfileRating(PlayerRatingUpdateDTO ratingUpdateDTO);

    PlayerProfileDTO getPlayerProfile(Long playerId);

    ResponseEntity<String> updatePlayerProfile(Long playerId, PlayerProfileUpdateDTO profileUpdateDTO);

    List<LeaderboardProfileDTO> getLeaderboard();
}