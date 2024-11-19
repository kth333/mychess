package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.dto.MatchmakingDTO;
import com.g1.mychess.match.dto.TournamentPlayerDTO;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.service.MatchmakingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of the {@link MatchmakingService} interface that handles the matchmaking process
 * for chess tournaments. This service supports various tournament formats, including Swiss,
 * Knockout, and Round Robin.
 */
@Service
public class MatchmakingServiceImpl implements MatchmakingService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;

    /**
     * Constructs a new instance of {@code MatchmakingServiceImpl}.
     *
     * @param matchRepository       the repository for managing {@link Match} entities
     * @param matchPlayerRepository the repository for managing {@link MatchPlayer} entities
     */
    @Autowired
    public MatchmakingServiceImpl(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
    }

    /**
     * Executes the matchmaking process based on the tournament format and participant details.
     *
     * @param matchmakingDTO   the data transfer object containing tournament details, participants, and the current round
     */
    @Override
    @Transactional
    public void runMatchmaking(MatchmakingDTO matchmakingDTO) {
        Long tournamentId = matchmakingDTO.getTournamentId();
        int currentRound = matchmakingDTO.getCurrentRound();
        Set<TournamentPlayerDTO> participants = matchmakingDTO.getParticipants();
        List<MatchPlayer> players = initializePlayers(participants, currentRound);
        List<Match> newMatches = createSwissSystemMatches(players, tournamentId, currentRound);
        matchRepository.saveAll(newMatches);
        saveMatchPlayers(newMatches);
    }

    /**
     * Initializes the players for the current round by converting participants to {@link MatchPlayer} objects.
     *
     * @param participants the set of participants in the tournament
     * @param currentRound the current round number
     * @return a list of {@link MatchPlayer} objects
     */
    private List<MatchPlayer> initializePlayers(Set<TournamentPlayerDTO> participants, int currentRound) {
        List<MatchPlayer> players = new ArrayList<>();

        for (TournamentPlayerDTO participant : participants) {
            MatchPlayer player = createMatchPlayer(participant, currentRound);
            players.add(player);
        }
        return players;
    }

    /**
     * Creates a {@link MatchPlayer} object for a specific round based on the given participant details.
     *
     * @param participant   the participant details
     * @param currentRound  the current round number
     * @return a {@link MatchPlayer} object with initialized data
     */
    private MatchPlayer createMatchPlayer(TournamentPlayerDTO participant, int currentRound) {
        MatchPlayer player = new MatchPlayer();
        player.setPlayerId(participant.getPlayerId());
        player.setGlickoRating(participant.getGlickoRating());
        player.setRatingDeviation(participant.getRatingDeviation());
        player.setVolatility(participant.getVolatility());
        player.setCurrentRound(currentRound);
        player.setPoints(getPlayerPoints(participant, currentRound));
        return player;
    }

    /**
     * Retrieves the points of a participant for the current round. If it is the first round, the points are initialized to zero.
     *
     * @param participant   the participant details
     * @param currentRound  the current round number
     * @return the points earned by the participant
     */
    private double getPlayerPoints(TournamentPlayerDTO participant, int currentRound) {
        if (currentRound == 1) return 0;

        MatchPlayer previousRoundPlayer = matchPlayerRepository.findByPlayerIdAndMatch_TournamentIdAndCurrentRound(
                participant.getPlayerId(), participant.getTournamentId(), currentRound - 1);

        return (previousRoundPlayer != null) ? previousRoundPlayer.getPoints() : 0;
    }

    /**
     * Creates matches for the Swiss system by pairing players based on their points and ratings.
     *
     * @param players       the list of players to be paired
     * @param tournamentId  the ID of the tournament
     * @param currentRound  the current round number
     * @return a list of {@link Match} objects representing the created matches
     */
    private List<Match> createSwissSystemMatches(List<MatchPlayer> players, Long tournamentId, int currentRound) {
        List<Match> matches = new ArrayList<>();
        sortPlayersByPointsAndRating(players);

        Set<Long> pairedPlayers = new HashSet<>();
        for (int i = 0; i < players.size() - 1; i += 2) {
            MatchPlayer player1 = players.get(i);
            MatchPlayer player2 = players.get(i + 1);

            if (canPairPlayers(pairedPlayers, player1, player2)) {
                matches.add(createMatch(player1, player2, tournamentId, currentRound));
                pairedPlayers.add(player1.getPlayerId());
                pairedPlayers.add(player2.getPlayerId());
            }
        }
        if (players.size() % 2 != 0) handleBye(players.get(players.size() - 1), tournamentId, currentRound);

        return matches;
    }

    /**
     * Sorts the players by their points and Glicko ratings in descending order.
     *
     * @param players the list of players to be sorted
     */
    private void sortPlayersByPointsAndRating(List<MatchPlayer> players) {
        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getGlickoRating)
                .reversed());
    }

    /**
     * Checks whether two players can be paired for a match.
     *
     * @param pairedPlayers the set of already paired players
     * @param player1       the first player
     * @param player2       the second player
     * @return {@code true} if both players can be paired; {@code false} otherwise
     */
    private boolean canPairPlayers(Set<Long> pairedPlayers, MatchPlayer player1, MatchPlayer player2) {
        return !pairedPlayers.contains(player1.getPlayerId()) && !pairedPlayers.contains(player2.getPlayerId());
    }

    /**
     * Handles the bye scenario where a player has no opponent in the current round.
     *
     * @param byePlayer     the player receiving the bye
     * @param tournamentId  the ID of the tournament
     * @param currentRound  the current round number
     */
    private void handleBye(MatchPlayer byePlayer, Long tournamentId, int currentRound) {
        byePlayer.setPoints(byePlayer.getPoints() + 1);
        byePlayer.setResult(MatchPlayer.Result.WIN);

        Match byeMatch = createByeMatch(byePlayer, tournamentId, currentRound);
        matchRepository.save(byeMatch);
        matchPlayerRepository.save(byePlayer);
    }

    /**
     * Creates a match for a player who has received a bye.
     *
     * @param byePlayer     the player receiving the bye
     * @param tournamentId  the ID of the tournament
     * @param currentRound  the current round number
     * @return a {@link Match} object representing the bye match
     */
    private Match createByeMatch(MatchPlayer byePlayer, Long tournamentId, int currentRound) {
        Match match = new Match();
        match.setTournamentId(tournamentId);
        match.setScheduledTime(LocalDateTime.now().plusDays(1));
        match.setStatus(Match.MatchStatus.COMPLETED);
        match.setRoundNumber(currentRound);
        match.setParticipants(Collections.singleton(byePlayer));
        byePlayer.setMatch(match);
        return match;
    }

    /**
     * Creates a match between two players.
     *
     * @param player1       the first player
     * @param player2       the second player
     * @param tournamentId  the ID of the tournament
     * @param currentRound  the current round number
     * @return a {@link Match} object representing the created match
     */
    private Match createMatch(MatchPlayer player1, MatchPlayer player2, Long tournamentId, int currentRound) {
        Match match = new Match();
        match.setTournamentId(tournamentId);
        match.setScheduledTime(LocalDateTime.now().plusDays(1));
        match.setStatus(Match.MatchStatus.SCHEDULED);
        match.setRoundNumber(currentRound);

        match.setParticipants(new HashSet<>(Arrays.asList(player1, player2)));
        player1.setMatch(match);
        player2.setMatch(match);

        player1.setOpponentId(player2.getPlayerId());
        player2.setOpponentId(player1.getPlayerId());

        return match;
    }
}
