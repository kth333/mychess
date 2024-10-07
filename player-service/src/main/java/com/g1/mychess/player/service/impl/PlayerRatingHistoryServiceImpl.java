package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.service.PlayerRatingHistoryService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerRatingHistoryServiceImpl implements PlayerRatingHistoryService {

    private final PlayerRepository playerRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;

    public PlayerRatingHistoryServiceImpl(PlayerRepository playerRepository, PlayerRatingHistoryRepository playerRatingHistoryRepository) {
        this.playerRepository = playerRepository;
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")  // every day at midnight
    public void cleanupOldRatingHistories() {
        playerRatingHistoryRepository.deleteOldRatingHistories();
    }

    @Override
    @Transactional
    public void updatePlayerRatingHistory(PlayerRatingUpdateDTO ratingUpdateDTO) {
        Player player = playerRepository.findById(ratingUpdateDTO.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        PlayerRatingHistory ratingHistory = new PlayerRatingHistory();
        ratingHistory.setPlayer(player);
        ratingHistory.setGlickoRating(ratingUpdateDTO.getGlickoRating());
        ratingHistory.setRatingDeviation(ratingUpdateDTO.getRatingDeviation());
        ratingHistory.setVolatility(ratingUpdateDTO.getVolatility());
        ratingHistory.setDate(LocalDateTime.now());

        playerRatingHistoryRepository.save(ratingHistory);
    }
}