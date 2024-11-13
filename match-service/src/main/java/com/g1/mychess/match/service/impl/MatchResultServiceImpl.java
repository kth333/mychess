package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.exception.MatchNotFoundException;
import com.g1.mychess.match.exception.PlayerNotFoundException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.service.MatchResultService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementation of the {@link MatchResultService} interface.
 * Handles the finalization of a match by updating the results of the players based on the match outcome.
 * This includes handling the case of a draw or win/loss between participants.
 * Ensures the match status is updated and the match data is saved upon completion.
 *
 * <p>Throws:
 * - {@link MatchNotFoundException} if the match with the given ID does not exist.
 * - {@link PlayerNotFoundException} if any player involved in the match cannot be found in the list of participants.
 */
@Service
public class MatchResultServiceImpl implements MatchResultService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;

    /**
     * Constructs a {@link MatchResultServiceImpl} with the provided repositories.
     *
     * @param matchRepository Repository for accessing match data.
     * @param matchPlayerRepository Repository for accessing match player data.
     */
    public MatchResultServiceImpl(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
    }

    /**
     * Finalizes the match by determining the outcome (draw or win/loss) and updating the player results accordingly.
     * Updates the match status to completed and saves the match and player data.
     *
     * @param matchId The ID of the match to complete.
     * @param winnerPlayerId The ID of the winning player (relevant only if the match is not a draw).
     * @param loserPlayerId The ID of the losing player (relevant only if the match is not a draw).
     * @param isDraw A flag indicating whether the match is a draw.
     * @return A {@link ResponseEntity} containing the result message of the operation.
     *
     * @throws MatchNotFoundException if the match with the given ID cannot be found.
     * @throws PlayerNotFoundException if the given player IDs are not found among the match participants.
     */
    @Override
    @Transactional
    public ResponseEntity<String> completeMatch(Long matchId, Long winnerPlayerId, Long loserPlayerId, boolean isDraw) {
        Match match = getMatch(matchId);
        if (match.getStatus() == Match.MatchStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("Match is already completed.");
        }

        Set<MatchPlayer> participants = match.getParticipants();

        if (isDraw) handleDraw(participants);
        else handleWinLoss(participants, winnerPlayerId, loserPlayerId);

        finalizeMatchCompletion(match, participants);

        return ResponseEntity.ok("Match completed successfully.");
    }

    /**
     * Retrieves the match by its ID.
     *
     * @param matchId The ID of the match to retrieve.
     * @return The {@link Match} object associated with the given ID.
     * @throws MatchNotFoundException if the match cannot be found.
     */
    public Match getMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found with id: " + matchId));
    }

    /**
     * Handles the outcome where the match ends in a draw by assigning the draw result to both players.
     * Both players are awarded 0.5 points for the draw.
     *
     * @param participants The set of match participants to update.
     */
    private void handleDraw(Set<MatchPlayer> participants) {
        participants.forEach(player -> {
            player.setResult(MatchPlayer.Result.DRAW);
            player.setPoints(player.getPoints() + 0.5);
        });
    }

    /**
     * Handles the outcome where one player wins and another loses.
     * The winner is assigned a win result and 1 point, while the loser is assigned a loss result.
     *
     * @param participants The set of match participants to update.
     * @param winnerPlayerId The ID of the winning player.
     * @param loserPlayerId The ID of the losing player.
     * @throws PlayerNotFoundException if either player is not found among the participants.
     */
    private void handleWinLoss(Set<MatchPlayer> participants, Long winnerPlayerId, Long loserPlayerId) {
        MatchPlayer winner = getMatchPlayer(participants, winnerPlayerId);
        MatchPlayer loser = getMatchPlayer(participants, loserPlayerId);

        winner.setResult(MatchPlayer.Result.WIN);
        winner.setPoints(winner.getPoints() + 1);
        loser.setResult(MatchPlayer.Result.LOSS);
    }

    /**
     * Retrieves a {@link MatchPlayer} by player ID from the set of match participants.
     *
     * @param participants The set of match participants.
     * @param playerId The ID of the player to find.
     * @return The {@link MatchPlayer} associated with the given player ID.
     * @throws PlayerNotFoundException if the player cannot be found in the match participants.
     */
    private MatchPlayer getMatchPlayer(Set<MatchPlayer> participants, Long playerId) {
        return participants.stream()
                .filter(player -> player.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException("Player not found in match participants"));
    }

    /**
     * Finalizes the match completion by updating the match status to completed and saving the match and participants.
     *
     * @param match The match to complete.
     * @param participants The participants of the match to update.
     */
    private void finalizeMatchCompletion(Match match, Set<MatchPlayer> participants) {
        match.setStatus(Match.MatchStatus.COMPLETED);
        matchRepository.save(match);
        matchPlayerRepository.saveAll(participants);
    }
}
