package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;

public interface ProfileService {

    void updateProfileRating(PlayerRatingUpdateDTO ratingUpdateDTO);

    PlayerProfileDTO getPlayerProfile(Long playerId);
}