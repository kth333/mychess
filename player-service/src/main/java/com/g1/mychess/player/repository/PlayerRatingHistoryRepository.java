package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.PlayerRatingHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository interface for managing {@link PlayerRatingHistory} entities.
 * Provides methods to query and interact with player rating history data in the database.
 */
@Repository
public interface PlayerRatingHistoryRepository extends JpaRepository<PlayerRatingHistory, Long> {

	/**
	 * Retrieves the latest {@link PlayerRatingHistory} entry for a specific player.
	 *
	 * @param playerId the ID of the player whose latest rating history is to be retrieved.
	 * @return an {@link Optional} containing the latest {@link PlayerRatingHistory} for the player, or empty if not found.
	 */
	@Query("SELECT prh FROM PlayerRatingHistory prh WHERE prh.player.playerId = :playerId ORDER BY prh.date DESC")
	Optional<PlayerRatingHistory> findLatestRatingByPlayerId(@Param("playerId") Long playerId);

	/**
	 * Deletes rating history entries older than 7 days, except the latest one for each player.
	 * This method is used for cleanup purposes to keep the rating history data manageable.
	 *
	 * The method uses a native SQL query to identify and remove old ratings.
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM player_rating_history " +
			"WHERE date < NOW() - INTERVAL 7 DAY " +
			"AND id NOT IN (" +
			"   SELECT * FROM (SELECT MAX(id) " +
			"                 FROM player_rating_history " +
			"                 GROUP BY player_id) as latest)",
			nativeQuery = true)
	void deleteOldRatingHistories();
}
