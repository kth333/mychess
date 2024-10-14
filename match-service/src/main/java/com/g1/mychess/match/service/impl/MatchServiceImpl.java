package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.match.dto.TournamentDTO;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.service.MatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${tournament.service.url}")
    private String tournamentServiceUrl;

    public MatchServiceImpl(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository, WebClient.Builder webClientBuilder) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
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

        Set<Long> pairedPlayers = new HashSet<>();

        for (int i = 0; i < players.size() - 1; i += 2) {
            MatchPlayer player1 = createNewMatchPlayer(players.get(i), roundNumber);
            MatchPlayer player2 = createNewMatchPlayer(players.get(i + 1), roundNumber);

            if (pairedPlayers.contains(player1.getPlayerId()) || pairedPlayers.contains(player2.getPlayerId())) {
                continue;
            }

            Match match = new Match();
            match.setTournamentId(tournamentId);
            match.setScheduledTime(LocalDateTime.now().plusDays(1));
            match.setStatus(Match.MatchStatus.SCHEDULED);
            match.setRoundNumber(roundNumber);

            match.setParticipants(new HashSet<>(Arrays.asList(player1, player2)));
            player1.setMatch(match);
            player2.setMatch(match);

            player1.setOpponentId(player2.getPlayerId());
            player2.setOpponentId(player1.getPlayerId());

            matches.add(match);

            pairedPlayers.add(player1.getPlayerId());
            pairedPlayers.add(player2.getPlayerId());
        }

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

    public List<MatchPlayer> getAllOpponents(List<MatchPlayer> matchPlayerList) {
        List<MatchPlayer> opponents = new ArrayList<>();

        for (MatchPlayer matchPlayer : matchPlayerList) {
            opponents.add(matchPlayerRepository.findByPlayerIdAndMatchId(matchPlayer.getOpponentId(), matchPlayer.getMatch().getId()));
        }

        return opponents;
    }

    public double[] getAllResults(List<MatchPlayer> matchPlayerList) {
        double[] result = new double[matchPlayerList.size()];
        for (int i = 0; i < matchPlayerList.size(); i++) {
            if (matchPlayerList.get(i).getResult() == MatchPlayer.Result.WIN) {
                result[i] = 1.0;
            } else if (matchPlayerList.get(i).getResult() == MatchPlayer.Result.DRAW) {
                result[i] = 0.5;
            } else {
                result[i] = 0.0;
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void prepareNextRound(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);

        int currentRound = tournament.getCurrentRound();
        List<MatchPlayer> players = getOrCreateTournamentPlayers(tournament, currentRound);

        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getInitialRating)
                .reversed());

        if (isTournamentOver(tournament)) {
            List<MatchPlayer> matchPlayers = matchPlayerRepository.findByMatch_TournamentIdAndCurrentRound(tournamentId, tournament.getMaxRounds()); // what if matchPlayer is bye on last round
            for (MatchPlayer player : matchPlayers) {
                List<MatchPlayer> matchPlayerList = matchPlayerRepository.findByPlayerIdAndMatch_TournamentId(player.getPlayerId(), tournamentId);
                List<MatchPlayer> opponents = getAllOpponents(matchPlayerList);
                double[] results = getAllResults(matchPlayerList);
                calculatePlayerRatings(player, opponents, results);
            }
            updatePlayerRatings(matchPlayers);
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

    @Override
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

    @Override
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

        match.setStatus(Match.MatchStatus.COMPLETED);
        matchRepository.save(match);
        matchPlayerRepository.saveAll(participants);

        return ResponseEntity.ok("Match completed successfully.");
    }

    public static void calculatePlayerRatings(MatchPlayer player, List<MatchPlayer> opponents, double[] result) {
        // converting rating and rating deviation to glicko-2 scale
        double R = (player.getInitialRating() - 1500) / 173.7178;
        double RD = player.getInitialRatingDeviation() / 173.7178;

        double[] opponents_rating = new double[opponents.size()];
        double[] opponents_RD = new double[opponents.size()];

        for (int j = 0; j < opponents.size(); j++) {
            opponents_rating[j] = (opponents.get(j).getInitialRating() - 1500) / 173.7178;
            opponents_RD[j] = opponents.get(j).getInitialRatingDeviation() / 173.7178;
            System.out.println(opponents_rating[j] + " " + opponents_RD[j]);
        }

        double delta = calculate_delta(R, opponents_rating, opponents_RD, result);
        System.out.println("delta = " + delta);
        double v = calculate_v(R, opponents_rating, opponents_RD);
        System.out.println("v = " + v);

        double newVolatility = calculate_volatility(RD, player.getInitialVolatility(), v, delta * delta);
        double newRatingDeviation = calculateNewRatingDeviation(RD, newVolatility, v);
        double newRating = calculateNewRating(R, newRatingDeviation, delta, v);

        player.setNewVolatility(newVolatility);
        player.setNewRatingDeviation(173.7178 * newRatingDeviation);
        player.setNewRating(173.7178 * newRating + 1500);
    }

    public void updatePlayerRatings(List<MatchPlayer> players) {
        for (MatchPlayer player : players) {
            PlayerRatingUpdateDTO playerRatingUpdate = new PlayerRatingUpdateDTO(
                    player.getPlayerId(),
                    player.getNewRating(),
                    player.getNewRatingDeviation(),
                    player.getNewVolatility(),
                    player.getMatch().getTournamentId(),
                    player.getResult().name()
            );
            updatePlayerProfileAndRatingHistory(playerRatingUpdate);
        }
    }

    public static double calculate_g(double RD) {
        return 1.0 / Math.sqrt(1.0 + (3.0 * RD * RD) / (Math.PI * Math.PI));
    }

    public static double calculate_E(double R, double Rj, double RDj) {
        return 1.0 / (1.0 + Math.exp(-calculate_g(RDj) * (R - Rj)));
    }

    public static double calculate_v(double R, double[] opponents_rating, double[] opponents_RD) {
        double v_inverse = 0;
        for (int j = 0; j < opponents_rating.length; j++) {
            double Rj = opponents_rating[j];
            double RDj = opponents_RD[j];
            double g_RDj = calculate_g(RDj);
            double E_R_Rj = calculate_E(R, Rj, RDj);
            v_inverse += g_RDj * g_RDj * E_R_Rj * (1 - E_R_Rj);
        }
        return 1 / v_inverse;
    }

    public static double calculate_delta(double R, double[] opponents_rating, double[] opponents_RD, double[] results) {
        double temp = 0;
        for (int j = 0; j < opponents_rating.length; j++) {
            double Rj = opponents_rating[j];
            double RDj = opponents_RD[j];
            double g_RDj = calculate_g(RDj);
            double E_R_Rj = calculate_E(R, Rj, RDj);
            temp += g_RDj * (results[j] - E_R_Rj);
        }
        return calculate_v(R, opponents_rating, opponents_RD) * temp;
    }

    public static double calculate_function(double x, double delta_squared, double RD_squared, double v, double A) {
        double expX = Math.exp(x);

        double numerator = expX * Math.pow(delta_squared - RD_squared - v - expX, 2);
        double denominator = Math.pow(RD_squared + v + expX, 2);

        double firstPart = numerator / denominator;
        double secondPart = (x - A) / (0.5 * 0.5);

        return firstPart - secondPart;
    }

    public static double calculate_volatility(double RD, double volatility, double v, double delta_squared) {
        double A = Math.log(Math.pow(volatility, 2));
        double B;
        double convergence_tolerance = 0.000001;

        if (delta_squared > (RD * RD) + v) {
            B = Math.log(delta_squared - (RD * RD) - v);
        } else {
            int k = 1;
            while (calculate_function(A - k * 0.5, delta_squared, RD * RD, v, A) < 0) { // tau is set at 0.5
                k++;
            }
            B = A - k * 0.5;
        }

        double f_A = calculate_function(A, delta_squared, RD * RD, v, A);
        double f_B = calculate_function(B, delta_squared, RD * RD, v, A);

        while (Math.abs(B - A) > convergence_tolerance) {
            double C = A + ((A - B) * f_A) / (f_B - f_A);
            double f_C = calculate_function(C, delta_squared, RD * RD, v, A);

            if (f_C * f_B <= 0) {
                A = B;
                f_A = f_B;
            } else {
                f_A /= 2;
            }
            B = C;
            f_B = f_C;
        }

        return Math.exp(A / 2);
    }

    private static double calculateNewRatingDeviation(double RD, double newVolatility, double v) {
        double pre_rating_RD = Math.sqrt(RD * RD + newVolatility * newVolatility);
        return 1 / Math.sqrt((1 / (pre_rating_RD * pre_rating_RD)) + (1 / v));
    }

    private static double calculateNewRating(double R, double newRD, double delta, double v) {
        return R + newRD * newRD * delta / v;
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

    private void calculateFinalStandings(Long tournamentId) {
        // Implement logic to calculate and store final standings
    }

    private void updatePlayerProfiles(Long tournamentId) {
        // Implement logic to update player profiles
    }

    @Override
    public TournamentDTO getTournamentDetails(Long tournamentId) {
        return webClientBuilder.build()
                .get()
                .uri(tournamentServiceUrl + "/api/v1/tournaments/public/" + tournamentId)
                .retrieve()
                .bodyToMono(TournamentDTO.class)
                .block();
    }

    @Override
    public List<Match> findAllMatchByTournament(Long tournamentId) {
        return matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
    }

    @Override
    public List<Match> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber) {
        matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        return matchRepository.findByTournamentIdAndRoundNumber(tournamentId, roundNumber)
                .orElseThrow(() -> new TournamentRoundNotFoundException("Tournament with id = " + tournamentId + " does not have round = " + roundNumber));
    }
}