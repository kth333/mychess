package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.PlayerRatingHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PlayerRatingHistoryRepository extends JpaRepository<PlayerRatingHistory, Long> {

	@Query("SELECT prh FROM PlayerRatingHistory prh WHERE prh.player.id = :playerId ORDER BY prh.date DESC")
	Optional<PlayerRatingHistory> findLatestRatingByPlayerId(@Param("playerId") Long playerId);

	// Query to delete ratings older than 7 days
	@Query("DELETE FROM PlayerRatingHistory prh WHERE prh.date < :date")
	void deleteRatingsOlderThan(@Param("date") LocalDateTime date);
}
