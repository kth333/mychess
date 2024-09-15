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

    // Schedule the task to run daily to delete ratings older than 7 days
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void deleteOldRatingHistories() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        playerRatingHistoryRepository.deleteRatingsOlderThan(sevenDaysAgo);
    }
}