package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.client.PlayerServiceClient;
import com.g1.mychess.match.dto.MatchmakingDTO;
import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.service.Glicko2RatingService;
import com.g1.mychess.match.service.MatchTimeService;
import com.g1.mychess.match.service.TournamentFinalisationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link TournamentFinalisationService} interface.
 * Handles finalizing a tournament by validating completion, calculating player ratings using Glicko2,
 * and updating player profiles with new ratings.
 */
@Service
public class TournamentFinalisationServiceImpl implements TournamentFinalisationService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final Glicko2RatingService glicko2RatingService;
    private final PlayerServiceClient playerServiceClient;

    public TournamentFinalisationServiceImpl(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository, PlayerServiceClient playerServiceClient, Glicko2RatingService glicko2RatingService) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.playerServiceClient = playerServiceClient;
        this.glicko2RatingService = glicko2RatingService;
    }

    /**
     * Finalizes the tournament by validating if it can be completed and updating players' ratings.
     *
     * @param matchmakingDTO DTO containing tournament details (e.g., ID, rounds).
     * @throws IllegalStateException if tournament or matches are incomplete.
     */
    @Override
    @Transactional
    public void finalizeTournament(MatchmakingDTO matchmakingDTO) {
        validateTournamentCompletion(matchmakingDTO);

        List<MatchPlayer> matchPlayers = getFinalRoundPlayers(matchmakingDTO.getTournamentId(), matchmakingDTO.getMaxRounds());

        matchPlayers.forEach(player -> {
            PlayerRatingUpdateDTO ratingUpdate = calculatePlayerRating(player, matchmakingDTO.getTournamentId());
            playerServiceClient.updatePlayerProfileRating(ratingUpdate);
        });
    }

    /**
     * Validates if the tournament can be completed, ensuring all rounds are finished.
     *
     * @param matchmakingDTO DTO containing tournament details.
     * @throws IllegalStateException if rounds are not completed.
     */
    private void validateTournamentCompletion(MatchmakingDTO matchmakingDTO) {
        if (matchmakingDTO.getCurrentRound() < matchmakingDTO.getMaxRounds()) {
            throw new IllegalStateException("Cannot complete tournament. Rounds not completed yet.");
        }
        checkAllMatchesCompleted(matchmakingDTO.getTournamentId());
    }

    /**
     * Ensures that all matches in the tournament are marked as completed.
     *
     * @param tournamentId ID of the tournament.
     * @throws IllegalStateException if any match is not completed.
     * @throws TournamentNotFoundException if tournament with the given ID does not exist.
     */
    private void checkAllMatchesCompleted(Long tournamentId) {
        matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."))
                .forEach(match -> {
                    if (match.getStatus() != Match.MatchStatus.COMPLETED) {
                        throw new IllegalStateException("Cannot complete tournament. Matches not completed yet.");
                    }
                });
    }

    /**
     * Retrieves the players who participated in the final round of the tournament.
     *
     * @param tournamentId ID of the tournament.
     * @param maxRounds    The maximum number of rounds to identify the final round.
     * @return List of MatchPlayer objects for players in the final round.
     */
    private List<MatchPlayer> getFinalRoundPlayers(Long tournamentId, int maxRounds) {
        return matchPlayerRepository.findByMatch_TournamentIdAndCurrentRound(tournamentId, maxRounds);
    }

    /**
     * Calculates the rating for a given player using the Glicko2 rating system.
     *
     * @param player        The player whose rating is to be calculated.
     * @param tournamentId  ID of the tournament.
     * @return DTO containing the updated rating of the player.
     */
    private PlayerRatingUpdateDTO calculatePlayerRating(MatchPlayer player, Long tournamentId) {
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findByPlayerIdAndMatch_TournamentId(player.getPlayerId(), tournamentId);
        List<MatchPlayer> opponents = getAllOpponents(matchPlayerList);
        double[] results = getAllResults(matchPlayerList);

        return glicko2RatingService.calculatePlayerRatings(player, opponents, results);
    }

    /**
     * Retrieves all opponents for the given list of match players.
     *
     * @param matchPlayerList The list of match players involving the given player.
     * @return List of opponent players.
     */
    private List<MatchPlayer> getAllOpponents(List<MatchPlayer> matchPlayerList) {
        return matchPlayerList.stream()
                .filter(matchPlayer -> matchPlayer.getOpponentId() != null)
                .map(matchPlayer -> matchPlayerRepository.findByPlayerIdAndMatchId(matchPlayer.getOpponentId(), matchPlayer.getMatch().getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the results of the matches for a given list of match players.
     *
     * @param matchPlayerList The list of match players.
     * @return Array of doubles representing the results (1.0 = win, 0.5 = draw, 0.0 = loss).
     */
    private double[] getAllResults(List<MatchPlayer> matchPlayerList) {
        return matchPlayerList.stream()
                .mapToDouble(matchPlayer -> {
                    MatchPlayer.Result result = matchPlayer.getResult();
                    if (result == MatchPlayer.Result.WIN) {
                        return 1.0;
                    } else if (result == MatchPlayer.Result.DRAW) {
                        return 0.5;
                    } else {
                        return 0.0;
                    }
                })
                .toArray();
    }
}
