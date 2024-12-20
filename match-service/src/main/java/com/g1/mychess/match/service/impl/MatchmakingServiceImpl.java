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
 * MatchmakingServiceImpl is a service responsible for handling the matchmaking
 * process in a chess tournament. It implements the Swiss system of pairing players,
 * assigns points, handles "bye" scenarios, and saves match data to the database.
 */
@Service
public class MatchmakingServiceImpl implements MatchmakingService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;

    /**
     * Constructor for MatchmakingServiceImpl.
     *
     * @param matchRepository       Repository for Match entity
     * @param matchPlayerRepository Repository for MatchPlayer entity
     */
    @Autowired
    public MatchmakingServiceImpl(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
    }

    /**
     * Runs matchmaking based on the specified tournament type.
     * @param matchmakingDTO Data transfer object containing tournament details, current round, and participants.
     * @param tournamentFormat The type of tournament (e.g., "SWISS", "KNOCKOUT", "ROUND_ROBIN").
     */
    @Override
    @Transactional
    public void runMatchmaking(MatchmakingDTO matchmakingDTO, String tournamentFormat) {
        switch (tournamentFormat.toUpperCase()) {
            case "KNOCKOUT":
                generateKnockoutMatches(matchmakingDTO);
                break;
            case "ROUND_ROBIN":
                generateRoundRobinMatches(matchmakingDTO);
                break;
            case "SWISS":
            default:
                generateSwissMatches(matchmakingDTO);
                break;
        }
    }

    /**
     * Initializes a list of MatchPlayer entities for the current round.
     *
     * @param participants The set of participants in the tournament
     * @param currentRound The current round number
     * @return List of MatchPlayer objects
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
     * Creates a MatchPlayer object from a TournamentPlayerDTO for a specific round.
     *
     * @param participant The participant data transfer object
     * @param currentRound The current round number
     * @return A MatchPlayer object initialized with the player's data
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
     * Retrieves the points of a player for the current round.
     * If it is the first round, the player starts with 0 points.
     *
     * @param participant The participant data transfer object
     * @param currentRound The current round number
     * @return The player's points for the current round
     */
    private double getPlayerPoints(TournamentPlayerDTO participant, int currentRound) {
        if (currentRound == 1) return 0;

        MatchPlayer previousRoundPlayer = matchPlayerRepository.findByPlayerIdAndMatch_TournamentIdAndCurrentRound(
                participant.getPlayerId(), participant.getTournamentId(), currentRound - 1);

        return (previousRoundPlayer != null) ? previousRoundPlayer.getPoints() : 0;
    }

    /**
     * Runs the Swiss-system matchmaking, pairing players by points and ratings.
     */
    private void generateSwissMatches(MatchmakingDTO matchmakingDTO) {
        Long tournamentId = matchmakingDTO.getTournamentId();
        int currentRound = matchmakingDTO.getCurrentRound();
        Set<TournamentPlayerDTO> participants = matchmakingDTO.getParticipants();

        List<MatchPlayer> players = initializePlayers(participants, currentRound);
        List<Match> newMatches = createSwissSystemMatches(players, tournamentId, currentRound);

        matchRepository.saveAll(newMatches);
        saveMatchPlayers(newMatches);
    }

    /**
     * Generates matches for the knockout stage of a tournament.
     */
    private void generateKnockoutMatches(MatchmakingDTO matchmakingDTO) {
        Long tournamentId = matchmakingDTO.getTournamentId();
        int currentRound = matchmakingDTO.getCurrentRound();
        List<Match> currentRoundMatches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, currentRound)
                .orElseThrow(() -> new TournamentRoundNotFoundException("Tournament with id = " + tournamentId + " does not have round = " + currentRound));

        if (!isRoundComplete(currentRoundMatches)) return;

        List<MatchPlayer> winners = getWinnersFromMatches(currentRoundMatches);
        if (winners.size() == 1) return;

        int nextRound = currentRound + 1;
        for (int i = 0; i < winners.size(); i += 2) {
            if (i + 1 < winners.size()) {
                createMatch(tournamentId, nextRound, winners.get(i).getPlayerId(), winners.get(i + 1).getPlayerId());
            }
        }
    }

    /**
     * Generates all possible matches for a round-robin tournament.
     */
    private void generateRoundRobinMatches(MatchmakingDTO matchmakingDTO) {
        Long tournamentId = matchmakingDTO.getTournamentId();
        List<Long> playerIds = new ArrayList<>();
        matchmakingDTO.getParticipants().forEach(player -> playerIds.add(player.getPlayerId()));

        for (int i = 0; i < playerIds.size(); i++) {
            for (int j = i + 1; j < playerIds.size(); j++) {
                createMatch(tournamentId, 1, playerIds.get(i), playerIds.get(j));
            }
        }
    }

    /**
     * Helper method to check if all matches in a round are completed.
     */
    private boolean isRoundComplete(List<Match> matches) {
        return matches.stream().allMatch(match -> matchPlayerRepository.findByMatchId(match.getId())
                .stream().allMatch(player -> player.getResult() != null));
    }

    private List<MatchPlayer> getWinnersFromMatches(List<Match> matches) {
        List<MatchPlayer> winners = new ArrayList<>();
        for (Match match : matches) {
            matchPlayerRepository.findByMatchId(match.getId()).stream()
                    .filter(player -> player.getResult() == MatchPlayer.Result.WIN)
                    .findFirst()
                    .ifPresent(winners::add);
        }
        return winners;
    }

    /**
     * Helper method to create a match between two players.
     */
    private Match createMatch(Long tournamentId, int roundNumber, Long player1Id, Long player2Id) {
        Match match = new Match();
        match.setTournamentId(tournamentId);
        match.setRoundNumber(roundNumber);

        MatchPlayer player1 = new MatchPlayer();
        player1.setPlayerId(player1Id);
        player1.setMatch(match);

        MatchPlayer player2 = new MatchPlayer();
        player2.setPlayerId(player2Id);
        player2.setMatch(match);

        matchRepository.save(match);
        matchPlayerRepository.save(player1);
        matchPlayerRepository.save(player2);

        return match;
    }

    /**
     * Creates the Swiss system matches by pairing players based on their points and ratings.
     *
     * @param players The list of MatchPlayer objects
     * @param tournamentId The tournament ID
     * @param currentRound The current round number
     * @return A list of Match objects representing the scheduled matches
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
     * Checks if two players can be paired for a match.
     *
     * @param pairedPlayers Set of already paired players
     * @param player1 The first player
     * @param player2 The second player
     * @return true if both players can be paired, false otherwise
     */
    private boolean canPairPlayers(Set<Long> pairedPlayers, MatchPlayer player1, MatchPlayer player2) {
        return !pairedPlayers.contains(player1.getPlayerId()) && !pairedPlayers.contains(player2.getPlayerId());
    }

    /**
     * Handles the bye scenario for a player who does not have an opponent in the current round.
     * The player automatically wins and earns points.
     *
     * @param byePlayer The player who receives a bye
     * @param tournamentId The tournament ID
     * @param currentRound The current round number
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
     * @param byePlayer The player who receives a bye
     * @param tournamentId The tournament ID
     * @param currentRound The current round number
     * @return A Match object representing the bye match
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
     * @param player1 The first player
     * @param player2 The second player
     * @param tournamentId The tournament ID
     * @param currentRound The current round number
     * @return A Match object representing the scheduled match
     */
    private Match createMatch(MatchPlayer player1, MatchPlayer player2, Long tournamentId, int currentRound) {
        Match match = new Match();
        match.setTournamentId(tournamentId);
        match.setScheduledTime(LocalDateTime.now().plusDays(1));
        match.setStatus(Match.MatchStatus.SCHEDULED);
        match.setRoundNumber(currentRound);

        // Set the match players as participants
        match.setParticipants(new HashSet<>(Arrays.asList(player1, player2)));
        player1.setMatch(match);
        player2.setMatch(match);

        player1.setOpponentId(player2.getPlayerId());
        player2.setOpponentId(player1.getPlayerId());

        return match;
    }

    /**
     * Saves the players associated with the matches to the database.
     *
     * @param matches The list of matches to save players for
     */
    private void saveMatchPlayers(List<Match> matches) {
        List<MatchPlayer> matchPlayers = new ArrayList<>();
        for (Match match : matches) {
            matchPlayers.addAll(match.getParticipants());
        }
        matchPlayerRepository.saveAll(matchPlayers);
    }

    /**
     * Sorts players by their points and rating in descending order.
     *
     * @param players The list of players to sort
     */
    private void sortPlayersByPointsAndRating(List<MatchPlayer> players) {
        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getGlickoRating)
                .reversed());
    }
}
