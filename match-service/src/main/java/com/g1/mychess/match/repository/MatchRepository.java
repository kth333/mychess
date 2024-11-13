package com.g1.mychess.match.repository;

import com.g1.mychess.match.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Match} entity.
 * This interface extends {@link JpaRepository} to provide CRUD operations for the {@link Match} entity.
 * It also includes custom query methods for retrieving match data based on tournament IDs and round numbers.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    /**
     * Finds matches by the specified tournament ID.
     * This method retrieves all matches associated with a specific tournament.
     *
     * @param tournamentId The ID of the tournament to find matches for.
     * @return An {@link Optional} containing a list of matches for the given tournament ID.
     */
    Optional<List<Match>> findByTournamentId(Long tournamentId);

    /**
     * Finds the maximum round number for a given tournament.
     * This query returns the highest round number associated with the given tournament ID.
     *
     * @param tournamentId The ID of the tournament to find the max round number for.
     * @return An {@link Optional} containing the maximum round number for the tournament.
     */
    @Query("SELECT MAX(m.roundNumber) FROM Match m WHERE m.tournamentId = :tournamentId")
    Optional<Integer> findMaxRoundNumberByTournamentId(@Param("tournamentId") Long tournamentId);

    /**
     * Finds matches by both tournament ID and round number.
     * This method retrieves all matches for a specific tournament and round.
     *
     * @param tournamentId The ID of the tournament.
     * @param roundNumber The round number to find matches for.
     * @return An {@link Optional} containing a list of matches for the given tournament and round number.
     */
    Optional<List<Match>> findByTournamentIdAndRoundNumber(Long tournamentId, Integer roundNumber);

    List<Match> findByScheduledTimeBetween(LocalDateTime now, LocalDateTime oneHourLater);
}