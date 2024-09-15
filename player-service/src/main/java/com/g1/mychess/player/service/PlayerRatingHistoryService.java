package com.g1.mychess.player.service;

import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerRatingHistoryService {

    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;

    public PlayerRatingHistoryService(PlayerRatingHistoryRepository playerRatingHistoryRepository) {
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")  // every day at midnight
    public void cleanupOldRatingHistories() {
        playerRatingHistoryRepository.deleteOldRatingHistories();
    }
}