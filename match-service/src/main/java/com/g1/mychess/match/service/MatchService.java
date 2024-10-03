package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.TournamentDTO;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${tournament.service.url}")
    private String tournamentServiceUrl;

    public MatchService(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository, WebClient.Builder webClientBuilder) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.webClientBuilder = webClientBuilder;
    }

    private void calculateAndUpdatePlayerRatings(MatchPlayer winner, MatchPlayer loser, boolean isDraw) {
        // Implement based on Glicko-2 or other rating system
        double winnerScore = 1;
        double loserScore = 0;

        if (isDraw) {
            winnerScore = 0.5;
            loserScore = 0.5;
        }
        // for Winner
        double winner_d2 = calculate_D2(winner.getInitialRating(), loser.getInitialRating(), loser.getInitialRatingDeviation());
        double winnerNewGlickoRating = calculateNewRating(winner.getInitialRating(), winner.getInitialRatingDeviation(), loser.getInitialRating(), loser.getInitialRatingDeviation(), winnerScore, winner_d2);
        double winnerNewRatingDeviation = calculateNewRatingDeviation(winner.getInitialRatingDeviation(), winner_d2);

        // for Loser
        double loser_d2 = calculate_D2(loser.getInitialRating(), winner.getInitialRating(), winner.getInitialRatingDeviation());
        double loserNewGlickoRating = calculateNewRating(loser.getInitialRating(), loser.getInitialRatingDeviation(), winner.getInitialRating(), winner.getInitialRatingDeviation(), loserScore,loser_d2);
        double loserNewRatingDeviation = calculateNewRatingDeviation(loser.getInitialRatingDeviation(), loser_d2);

        // Not implemented yet
        // double newVolatility = calculateNewVolatility(matchPlayer.getInitialVolatility(), matchPlayer.getResult());

        // Update the matchPlayer with new values
        winner.setNewRating(winnerNewGlickoRating);
        winner.setNewRatingDeviation(winnerNewRatingDeviation);

        loser.setNewRating(loserNewGlickoRating);
        loser.setNewRatingDeviation(loserNewRatingDeviation);

        // Update the player's profile and rating history in PlayerService
        PlayerRatingUpdateDTO winnerRatingUpdate = new PlayerRatingUpdateDTO(
                winner.getPlayerId(),
                winnerNewGlickoRating,
                winnerNewRatingDeviation,
                winner.getInitialVolatility(),
                winner.getMatch().getTournamentId(),
                winner.getResult().name()
        );

        PlayerRatingUpdateDTO loserRatingUpdate = new PlayerRatingUpdateDTO(
                loser.getPlayerId(),
                loserNewGlickoRating,
                loserNewRatingDeviation,
                loser.getInitialVolatility(),
                loser.getMatch().getTournamentId(),
                loser.getResult().name()
        );

        updatePlayerProfileAndRatingHistory(winnerRatingUpdate);
        updatePlayerProfileAndRatingHistory(loserRatingUpdate);
    }

    public static double calculate_g(double RD) {
        return 1.0 / Math.sqrt(1.0 + (3.0 * RD * RD) / (Math.PI * Math.PI));
    }

    public static double calculate_E(double R, double Rj, double RDj) {
        return 1.0 / (1.0 + Math.pow(10.0, -calculate_g(RDj) * (R - Rj) / 400.0));
    }

    public static double calculate_D2(double R, double opponentsRating, double opponentsRatingDeviation) {
        double q = Math.log(10) / Math.log(Math.E) / 400; // constant q
        double g_RDj = calculate_g(opponentsRatingDeviation);
        double E_R_Rj = calculate_E(R, opponentsRating, opponentsRatingDeviation);
        return 1.0 / (q * q * g_RDj * g_RDj * E_R_Rj * (1 - E_R_Rj));
    }

    private double calculateNewRating(double initialRating, double currentRatingDeviation, double opponentRating, double opponentRatingDeviation, double result, double d2) {
        // Implement rating calculation logic here based on the result
        double q = Math.log(10) / Math.log(Math.E) / 400; // constant q

        return initialRating + (q / (1.0 / (currentRatingDeviation * currentRatingDeviation) + 1.0 / d2)) * (calculate_g(opponentRatingDeviation) * (result - calculate_E(initialRating, opponentRating, opponentRatingDeviation)));
    }

    private double calculateNewRatingDeviation(double initialRatingDeviation, double d2) {
        // Implement rating deviation calculation logic here
        return 1.0 / Math.sqrt(1.0 / (initialRatingDeviation * initialRatingDeviation) + 1.0 / d2);
    }

    private double calculateNewVolatility(double initialVolatility, MatchPlayer.Result result) {
        // Implement volatility calculation logic here
        // This is a placeholder
        return initialVolatility * 0.95; // Example logic: Adjust volatility slightly after a match
    }

    private void updatePlayerProfileAndRatingHistory(PlayerRatingUpdateDTO ratingUpdate) {
        webClientBuilder.build()
                .post()
                .uri(playerServiceUrl + "/api/v1/player/update-rating")
                .bodyValue(ratingUpdate)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Transactional
    public void runMatchmaking(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);

        int currentRound = 1;
        List<MatchPlayer> players = getOrCreateTournamentPlayers(tournament, currentRound);

        List<Match> newMatches = createSwissSystemMatches(players, tournamentId, currentRound);

        matchRepository.saveAll(newMatches);
    }

    private List<MatchPlayer> getOrCreateTournamentPlayers(TournamentDTO tournament, int roundNumber) {
        List<MatchPlayer> existingPlayers = matchPlayerRepository.findByMatch_TournamentIdAndCurrentRound(tournament.getId(), roundNumber);

        if (existingPlayers.isEmpty()) {
            return tournament.getParticipants().stream()
                    .map(participant -> {
                        MatchPlayer player = new MatchPlayer();
                        player.setPlayerId(participant.getPlayerId());
                        player.setInitialRating(participant.getGlickoRating());
                        player.setCurrentRound(roundNumber);
                        player.setPoints(0);
                        return player;
                    })
                    .collect(Collectors.toList());
        } else {
            return existingPlayers;
        }
    }

    private List<Match> createSwissSystemMatches(List<MatchPlayer> players, Long tournamentId, int roundNumber) {
        List<Match> matches = new ArrayList<>();

        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getInitialRating)
                .reversed());

        Set<Long> pairedPlayers = new HashSet<>(); // To track already paired players in this round

        for (int i = 0; i < players.size() - 1; i += 2) {
            MatchPlayer player1 = createNewMatchPlayer(players.get(i), roundNumber);
            MatchPlayer player2 = createNewMatchPlayer(players.get(i + 1), roundNumber);

            // Skip players who have already been paired in this round
            if (pairedPlayers.contains(player1.getPlayerId()) || pairedPlayers.contains(player2.getPlayerId())) {
                continue;
            }

            Match match = new Match();
            match.setTournamentId(tournamentId);
            match.setScheduledTime(LocalDateTime.now().plusDays(1));
            match.setStatus(Match.MatchStatus.SCHEDULED);
            match.setRoundNumber(roundNumber);

            // Set up bidirectional relationships
            match.setParticipants(new HashSet<>(Arrays.asList(player1, player2)));
            player1.setMatch(match);
            player2.setMatch(match);

            matches.add(match);

            pairedPlayers.add(player1.getPlayerId());
            pairedPlayers.add(player2.getPlayerId());
        }

        // Handle odd number of players by assigning a bye
        if (players.size() % 2 != 0 && !pairedPlayers.contains(players.get(players.size() - 1).getPlayerId())) {
            MatchPlayer byePlayer = createNewMatchPlayer(players.get(players.size() - 1), roundNumber);
            assignByeToPlayer(byePlayer, tournamentId);
        }

        return matches;
    }

    private MatchPlayer createNewMatchPlayer(MatchPlayer existingPlayer, int roundNumber) {
        MatchPlayer newPlayer = new MatchPlayer();
        newPlayer.setPlayerId(existingPlayer.getPlayerId());

        newPlayer.setInitialRating(existingPlayer.getNewRating());
        newPlayer.setInitialRatingDeviation(existingPlayer.getNewRatingDeviation());
        newPlayer.setInitialVolatility(existingPlayer.getNewVolatility());

        newPlayer.setCurrentRound(roundNumber);
        newPlayer.setPoints(existingPlayer.getPoints());

        return newPlayer;
    }

    private void assignByeToPlayer(MatchPlayer byePlayer, Long tournamentId) {
        byePlayer.setPoints(byePlayer.getPoints() + 1);

        Match byeMatch = new Match();
        byeMatch.setTournamentId(tournamentId);
        byeMatch.setScheduledTime(LocalDateTime.now().plusDays(1));
        byeMatch.setStatus(Match.MatchStatus.COMPLETED);
        byeMatch.setRoundNumber(byePlayer.getCurrentRound());

        byePlayer.setMatch(byeMatch);

        matchRepository.save(byeMatch);
        matchPlayerRepository.save(byePlayer);
    }

    @Transactional
    public void prepareNextRound(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);

        int currentRound = tournament.getCurrentRound();

        List<MatchPlayer> players = getOrCreateTournamentPlayers(tournament, currentRound);

        

        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints).reversed()
                .thenComparing(MatchPlayer::getInitialRating).reversed());

        if (isTournamentOver(tournament)) {
            finalizeTournament(tournamentId);
            return;
        }

        currentRound = determineCurrentRound(tournamentId) + 1;

        List<Match> nextRoundMatches = createSwissSystemMatches(players, tournamentId, currentRound);
        matchRepository.saveAll(nextRoundMatches);
    }

    private int determineCurrentRound(Long tournamentId) {
        return matchRepository.findMaxRoundNumberByTournamentId(tournamentId).orElse(0);
    }

    private boolean isTournamentOver(TournamentDTO tournament) {
        return tournament.getCurrentRound() >= tournament.getMaxRounds();
    }

    @Transactional
    public void finalizeTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        for (Match match : matches) {
            if (match.getStatus() != Match.MatchStatus.COMPLETED) {
                match.setStatus(Match.MatchStatus.COMPLETED);
                matchRepository.save(match);
            }
        }
        calculateFinalStandings(tournamentId);
        updatePlayerProfiles(tournamentId);
    }

    @Transactional
    public ResponseEntity<String> completeMatch(Long matchId, Long winnerPlayerId, Long loserPlayerId, boolean isDraw) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getStatus() == Match.MatchStatus.COMPLETED) {
            return ResponseEntity.badRequest().body("Match is already completed.");
        }

        Set<MatchPlayer> participants = match.getParticipants();

        MatchPlayer winner = participants.stream()
                .filter(mp -> mp.getPlayerId().equals(winnerPlayerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Winner not found in match participants"));

        MatchPlayer loser = participants.stream()
                .filter(mp -> mp.getPlayerId().equals(loserPlayerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Loser not found in match participants"));

        if (isDraw) {
            winner.setResult(MatchPlayer.Result.DRAW);
            loser.setResult(MatchPlayer.Result.DRAW);

            winner.setPoints(winner.getPoints() + 0.5);
            loser.setPoints(loser.getPoints() + 0.5);
        } else {
            winner.setResult(MatchPlayer.Result.WIN);
            loser.setResult(MatchPlayer.Result.LOSS);

            winner.setPoints(winner.getPoints() + 1);
        }

        calculateAndUpdatePlayerRatings(winner, loser, isDraw);
        match.setStatus(Match.MatchStatus.COMPLETED);
        matchRepository.save(match);
        matchPlayerRepository.saveAll(participants);

        return ResponseEntity.ok("Match completed successfully.");
    }

    private void calculateFinalStandings(Long tournamentId) {
        // Implement logic to calculate and store final standings
        // This might involve summing up points, breaking ties, etc.
        // Example:
        // List<PlayerStanding> standings = calculateStandings(tournamentId);
        // standingsRepository.saveAll(standings);
    }

    private void updatePlayerProfiles(Long tournamentId) {
        // Implement logic to update player profiles
        // For example, you might update each player's rating or add a tournament to their history
    }

    public TournamentDTO getTournamentDetails(Long tournamentId) {
        return webClientBuilder.build()
                .get()
                .uri(tournamentServiceUrl + "/api/v1/tournaments/public/" + tournamentId)
                .retrieve()
                .bodyToMono(TournamentDTO.class)
                .block();
    }

    public List<Match> findAllMatchByTournament(Long tournamentId) {
        return matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
    }

    public List<Match> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber) {
        matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        return matchRepository.findByTournamentIdAndRoundNumber(tournamentId, roundNumber)
                .orElseThrow(() -> new TournamentRoundNotFoundException("Tournament with id = " + tournamentId + " does not have round = " + roundNumber));
    }
}