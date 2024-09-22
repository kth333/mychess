package com.g1.mychess.match.repository;

import com.g1.mychess.match.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    // Add custom query methods if needed
    List<Match> findByTournamentId(Long tournamentId);

    @Query("SELECT MAX(m.roundNumber) FROM Match m WHERE m.tournamentId = :tournamentId")
    Optional<Integer> findMaxRoundNumberByTournamentId(@Param("tournamentId") Long tournamentId);
}