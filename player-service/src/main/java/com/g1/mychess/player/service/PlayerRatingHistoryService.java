package com.g1.mychess.player.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.g1.mychess.player.dto.PlayerRatingHistoryDTO;

import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;

public interface PlayerRatingHistoryService {

    void cleanupOldRatingHistories();

    void updatePlayerRatingHistory(PlayerRatingUpdateDTO ratingUpdateDTO);

    ResponseEntity<List<PlayerRatingHistoryDTO>> getPlayerRatingHistory(Long playerId);

}