package com.g1.mychess.player.service.impl;

import com.g1.mychess.player.dto.PlayerRatingHistoryDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.exception.PlayerNotFoundException;
import com.g1.mychess.player.mapper.PlayerMapper;
import com.g1.mychess.player.model.Player;
import com.g1.mychess.player.model.PlayerRatingHistory;
import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
import com.g1.mychess.player.repository.PlayerRepository;
import com.g1.mychess.player.service.PlayerRatingHistoryService;
import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the PlayerRatingHistoryService interface.
 * This service handles operations related to player rating histories,
 * including creation, retrieval, and cleanup of old rating histories.
 */
@Service
public class PlayerRatingHistoryServiceImpl implements PlayerRatingHistoryService {

    private final PlayerRepository playerRepository;
    private final PlayerRatingHistoryRepository playerRatingHistoryRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param playerRepository Repository for Player entities.
     * @param playerRatingHistoryRepository Repository for PlayerRatingHistory entities.
     */
    public PlayerRatingHistoryServiceImpl(PlayerRepository playerRepository, PlayerRatingHistoryRepository playerRatingHistoryRepository) {
        this.playerRepository = playerRepository;
    }

    @Autowired
    public void setPlayerRatingHistoryRepository(PlayerRatingHistoryRepository playerRatingHistoryRepository) {
        this.playerRatingHistoryRepository = playerRatingHistoryRepository;
    }

    /**
     * Scheduled task to clean up old player rating histories.
     * Runs every day at midnight.
     */
    @Override
    @Scheduled(cron = "0 0 0 * * ?")  // every day at midnight
    public void cleanupOldRatingHistories() {
        playerRatingHistoryRepository.deleteOldRatingHistories();
    }

    /**
     * Updates a player's rating history with new Glicko rating details.
     *
     * @param ratingUpdateDTO Data Transfer Object containing rating update details.
     */
    @Override
    @Transactional
    public void updatePlayerRatingHistory(PlayerRatingUpdateDTO ratingUpdateDTO) {
        Player player = getPlayerById(ratingUpdateDTO.getPlayerId());

        PlayerRatingHistory ratingHistory = new PlayerRatingHistory();
        ratingHistory.setPlayer(player);
        ratingHistory.setGlickoRating(ratingUpdateDTO.getGlickoRating());
        ratingHistory.setRatingDeviation(ratingUpdateDTO.getRatingDeviation());
        ratingHistory.setVolatility(ratingUpdateDTO.getVolatility());
        ratingHistory.setDate(LocalDateTime.now());

        playerRatingHistoryRepository.save(ratingHistory);
    }

    /**
     * Retrieves the rating history of a specific player.
     *
     * @param playerId ID of the player whose rating history is to be retrieved.
     * @return ResponseEntity containing a list of PlayerRatingHistoryDTO objects.
     */
    @Override
    public ResponseEntity<List<PlayerRatingHistoryDTO>> getPlayerRatingHistory(Long playerId){
        Player player = getPlayerById(playerId);
        List<PlayerRatingHistory> ratingHistory = player.getRatingHistory();
        List<PlayerRatingHistoryDTO> ratingHistoryDTOs = PlayerMapper.toPlayerRatingHistoryDTOList(ratingHistory);

        return ResponseEntity.ok(ratingHistoryDTOs);
    }

    /**
     * Retrieves a player entity by its ID.
     *
     * @param playerId ID of the player.
     * @return Player entity.
     * @throws PlayerNotFoundException if the player with the given ID is not found.
     */
    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id " + playerId));
    }
}