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

@Repository
public interface PlayerRatingHistoryRepository extends JpaRepository<PlayerRatingHistory, Long> {

	@Query("SELECT prh FROM PlayerRatingHistory prh WHERE prh.player.playerId = :playerId ORDER BY prh.date DESC")
	Optional<PlayerRatingHistory> findLatestRatingByPlayerId(@Param("playerId") Long playerId);

	// Query to delete ratings older than 7 days except the latest
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
