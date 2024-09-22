package com.g1.mychess.match.repository;

import com.g1.mychess.match.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    // Add custom query methods if needed
    List<Match> findByTournamentId(Long tournamentId);
}