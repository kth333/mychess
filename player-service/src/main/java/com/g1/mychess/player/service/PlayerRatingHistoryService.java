package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;

public interface PlayerRatingHistoryService {

    void cleanupOldRatingHistories();

    void updatePlayerRatingHistory(PlayerRatingUpdateDTO ratingUpdateDTO);
}