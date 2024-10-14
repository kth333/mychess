package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.match.dto.TournamentDTO;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.service.Glicko2RatingService;
import com.g1.mychess.match.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Glicko2RatingService glicko2RatingService;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${tournament.service.url}")
    private String tournamentServiceUrl;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, MatchPlayerRepository matchPlayerRepository, WebClient.Builder webClientBuilder, Glicko2RatingService glicko2RatingService) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.webClientBuilder = webClientBuilder;
        this.glicko2RatingService = glicko2RatingService;
    }

    @Override
    @Transactional
    public void runMatchmaking(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);

        int currentRound = 1;
        List<MatchPlayer> players = initializeTournamentPlayersForRound(tournament, currentRound);

        List<Match> newMatches = createSwissSystemMatches(players, tournamentId, currentRound);

        matchRepository.saveAll(newMatches);
    }

    private List<MatchPlayer> initializeTournamentPlayersForRound(TournamentDTO tournament, int roundNumber) {
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
        players = sortPlayersByPointsAndRating(players);

        Set<Long> pairedPlayers = new HashSet<>();
        for (int i = 0; i < players.size() - 1; i += 2) {
            Match match = createMatch(players.get(i), players.get(i + 1), tournamentId, roundNumber, pairedPlayers);
            if (match != null) matches.add(match);
        }

        handleByeIfNeeded(players, pairedPlayers, tournamentId, roundNumber);
        return matches;
    }

    private List<MatchPlayer> sortPlayersByPointsAndRating(List<MatchPlayer> players) {
        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getInitialRating)
                .reversed());
        return players;
    }

    private Match createMatch(MatchPlayer player1, MatchPlayer player2, Long tournamentId, int roundNumber, Set<Long> pairedPlayers) {
        if (pairedPlayers.contains(player1.getPlayerId()) || pairedPlayers.contains(player2.getPlayerId())) return null;

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

        pairedPlayers.add(player1.getPlayerId());
        pairedPlayers.add(player2.getPlayerId());

        return match;
    }

    private void handleByeIfNeeded(List<MatchPlayer> players, Set<Long> pairedPlayers, Long tournamentId, int roundNumber) {
        if (players.size() % 2 != 0 && !pairedPlayers.contains(players.get(players.size() - 1).getPlayerId())) {
            MatchPlayer byePlayer = cloneMatchPlayerForNextRound(players.get(players.size() - 1), roundNumber);
            assignByeToPlayer(byePlayer, tournamentId);
        }
    }

    private MatchPlayer cloneMatchPlayerForNextRound(MatchPlayer existingPlayer, int roundNumber) {
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

        List<MatchPlayer> players = initializeAndSortPlayersForRound(tournament, tournament.getCurrentRound());

        if (isTournamentOver(tournament)) {
            throw new IllegalStateException("Cannot start the next round. The tournament has reached the maximum number of rounds. Please finalize the tournament.");
        }
        startNextRound(tournamentId, players);
    }

    private List<MatchPlayer> initializeAndSortPlayersForRound(TournamentDTO tournament, int currentRound) {
        List<MatchPlayer> players = initializeTournamentPlayersForRound(tournament, currentRound);

        // Sort players by points and rating
        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getInitialRating)
                .reversed());

        return players;
    }

    private boolean isTournamentOver(TournamentDTO tournament) {
        return tournament.getCurrentRound() >= tournament.getMaxRounds();
    }

    private void startNextRound(Long tournamentId, List<MatchPlayer> players) {
        int currentRound = determineCurrentRound(tournamentId) + 1;
        List<Match> nextRoundMatches = createSwissSystemMatches(players, tournamentId, currentRound);

        matchRepository.saveAll(nextRoundMatches);
    }

    private int determineCurrentRound(Long tournamentId) {
        return matchRepository.findMaxRoundNumberByTournamentId(tournamentId).orElse(0);
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

    @Override
    @Transactional
    public void finalizeTournament(Long tournamentId) {
        TournamentDTO tournament = getTournamentDetails(tournamentId);

        if (!isTournamentOver(tournament)) {
            throw new IllegalStateException("Cannot complete tournament. Rounds not completed yet.");
        }

        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        for (Match match : matches) {
            if (match.getStatus() == Match.MatchStatus.SCHEDULED) {
                throw new IllegalStateException("Cannot complete tournament. Matches not completed yet");
            }
        }

        List<MatchPlayer> matchPlayers = matchPlayerRepository.findByMatch_TournamentIdAndCurrentRound(tournamentId, tournament.getMaxRounds()); // what if matchPlayer is bye on last round
        for (MatchPlayer player : matchPlayers) {
            List<MatchPlayer> matchPlayerList = matchPlayerRepository.findByPlayerIdAndMatch_TournamentId(player.getPlayerId(), tournamentId);
            List<MatchPlayer> opponents = getAllOpponents(matchPlayerList);
            double[] results = getAllResults(matchPlayerList);
            glicko2RatingService.calculatePlayerRatings(player, opponents, results);
        }
        updatePlayerRatings(matchPlayers);
    }

    public void updatePlayerRatings(List<MatchPlayer> players) {
        for (MatchPlayer player : players) {
            PlayerRatingUpdateDTO playerRatingUpdate = new PlayerRatingUpdateDTO(
                    player.getPlayerId(),
                    player.getNewRating(),
                    player.getNewRatingDeviation(),
                    player.getNewVolatility()
            );
            updatePlayerProfileRating(playerRatingUpdate);
        }
    }

    private void updatePlayerProfileRating(PlayerRatingUpdateDTO ratingUpdate) {
        webClientBuilder.build()
                .post()
                .uri(playerServiceUrl + "/api/v1/player/update-rating")
                .bodyValue(ratingUpdate)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
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
    @Transactional
    public List<MatchDTO> findAllMatchByTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
        return convertToDTOList(matches);
    }

    // Convert a list of Match objects to a list of MatchDTOs
    public List<MatchDTO> convertToDTOList(List<Match> matches) {
        return matches.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<MatchDTO> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber) {
        matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        List<Match> matches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, roundNumber)
                .orElseThrow(() -> new TournamentRoundNotFoundException("Tournament with id = " + tournamentId + " does not have round = " + roundNumber));

        return convertToDTOList(matches);
    }

    private MatchDTO convertToDTO(Match match) {
        return MatchDTO.fromEntity(match);
    }
}