package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.client.PlayerServiceClient;
import com.g1.mychess.match.dto.*;
import com.g1.mychess.match.exception.MatchNotFoundException;
import com.g1.mychess.match.exception.TournamentNotFoundException;
import com.g1.mychess.match.exception.TournamentRoundNotFoundException;
import com.g1.mychess.match.mapper.MatchMapper;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.service.Glicko2RatingService;
import com.g1.mychess.match.service.MatchService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final Glicko2RatingService glicko2RatingService;
    private final PlayerServiceClient playerServiceClient;

    @Autowired
    public MatchServiceImpl(
            MatchRepository matchRepository,
            MatchPlayerRepository matchPlayerRepository,
            Glicko2RatingService glicko2RatingService,
            PlayerServiceClient playerServiceClient) {
        this.matchRepository = matchRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.glicko2RatingService = glicko2RatingService;
        this.playerServiceClient = playerServiceClient;
    }

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

    private List<MatchPlayer> initializePlayers(Set<TournamentPlayerDTO> participants, int currentRound) {
        List<MatchPlayer> players = new ArrayList<>();

        for (TournamentPlayerDTO participant : participants) {
            MatchPlayer player = new MatchPlayer();
            player.setPlayerId(participant.getPlayerId());
            player.setGlickoRating(participant.getGlickoRating());
            player.setRatingDeviation(participant.getRatingDeviation());
            player.setVolatility(participant.getVolatility());
            player.setCurrentRound(currentRound);
            player.setPoints(getPlayerPoints(participant, currentRound));
            players.add(player);
        }
        return players;
    }

    private double getPlayerPoints(TournamentPlayerDTO participant, int currentRound) {
        if (currentRound == 1) return 0;

        MatchPlayer previousRoundPlayer = matchPlayerRepository.findByPlayerIdAndMatch_TournamentIdAndCurrentRound(
                participant.getPlayerId(), participant.getTournamentId(), currentRound - 1);

        return (previousRoundPlayer != null) ? previousRoundPlayer.getPoints() : 0;
    }

    private List<Match> createSwissSystemMatches(List<MatchPlayer> players, Long tournamentId, int currentRound) {
        List<Match> matches = new ArrayList<>();
        players = sortPlayersByPointsAndRating(players);

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

    private boolean canPairPlayers(Set<Long> pairedPlayers, MatchPlayer player1, MatchPlayer player2) {
        return !pairedPlayers.contains(player1.getPlayerId()) && !pairedPlayers.contains(player2.getPlayerId());
    }

    private void handleBye(MatchPlayer byePlayer, Long tournamentId, int currentRound) {
        byePlayer.setPoints(byePlayer.getPoints() + 1);
        byePlayer.setResult(MatchPlayer.Result.WIN);

        Match byeMatch = createByeMatch(byePlayer, tournamentId, currentRound);
        matchRepository.save(byeMatch);
        matchPlayerRepository.save(byePlayer);
    }

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

    private void saveMatchPlayers(List<Match> matches) {
        List<MatchPlayer> matchPlayers = new ArrayList<>();
        for (Match match : matches) {
            matchPlayers.addAll(match.getParticipants());
        }
        matchPlayerRepository.saveAll(matchPlayers);
    }

    private List<MatchPlayer> sortPlayersByPointsAndRating(List<MatchPlayer> players) {
        players.sort(Comparator.comparingDouble(MatchPlayer::getPoints)
                .thenComparing(MatchPlayer::getGlickoRating)
                .reversed());
        return players;
    }

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

    private Match getMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found with id: " + matchId));
    }

    private void handleDraw(Set<MatchPlayer> participants) {
        participants.forEach(player -> {
            player.setResult(MatchPlayer.Result.DRAW);
            player.setPoints(player.getPoints() + 0.5);
        });
    }

    private void handleWinLoss(Set<MatchPlayer> participants, Long winnerPlayerId, Long loserPlayerId) {
        MatchPlayer winner = getMatchPlayer(participants, winnerPlayerId);
        MatchPlayer loser = getMatchPlayer(participants, loserPlayerId);

        winner.setResult(MatchPlayer.Result.WIN);
        winner.setPoints(winner.getPoints() + 1);
        loser.setResult(MatchPlayer.Result.LOSS);
    }

    private MatchPlayer getMatchPlayer(Set<MatchPlayer> participants, Long playerId) {
        return participants.stream()
                .filter(player -> player.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found in match participants"));
    }

    private void finalizeMatchCompletion(Match match, Set<MatchPlayer> participants) {
        match.setStatus(Match.MatchStatus.COMPLETED);
        matchRepository.save(match);
        matchPlayerRepository.saveAll(participants);
    }

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

    private void validateTournamentCompletion(MatchmakingDTO matchmakingDTO) {
        if (matchmakingDTO.getCurrentRound() < matchmakingDTO.getMaxRounds()) {
            throw new IllegalStateException("Cannot complete tournament. Rounds not completed yet.");
        }
        checkAllMatchesCompleted(matchmakingDTO.getTournamentId());
    }

    private void checkAllMatchesCompleted(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
        matches.forEach(match -> {
            if (match.getStatus() != Match.MatchStatus.COMPLETED) {
                throw new IllegalStateException("Cannot complete tournament. Matches not completed yet.");
            }
        });
    }

    private List<MatchPlayer> getFinalRoundPlayers(Long tournamentId, int maxRounds) {
        return matchPlayerRepository.findByMatch_TournamentIdAndCurrentRound(tournamentId, maxRounds);
    }

    private PlayerRatingUpdateDTO calculatePlayerRating(MatchPlayer player, Long tournamentId) {
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findByPlayerIdAndMatch_TournamentId(player.getPlayerId(), tournamentId);
        List<MatchPlayer> opponents = getAllOpponents(matchPlayerList);
        double[] results = getAllResults(matchPlayerList);

        return glicko2RatingService.calculatePlayerRatings(player, opponents, results);
    }

    private List<MatchPlayer> getAllOpponents(List<MatchPlayer> matchPlayerList) {
        List<MatchPlayer> opponents = new ArrayList<>();

        for (MatchPlayer matchPlayer : matchPlayerList) {
            if (matchPlayer.getOpponentId() != null) {
                MatchPlayer opponent = matchPlayerRepository.findByPlayerIdAndMatchId(matchPlayer.getOpponentId(), matchPlayer.getMatch().getId());
                if (opponent != null) {
                    opponents.add(opponent);
                }
            }
        }

        return opponents;
    }

    private double[] getAllResults(List<MatchPlayer> matchPlayerList) {
        double[] results = new double[matchPlayerList.size()];
        for (int i = 0; i < matchPlayerList.size(); i++) {
            MatchPlayer.Result result = matchPlayerList.get(i).getResult();
            if (result == MatchPlayer.Result.WIN) {
                results[i] = 1.0;
            } else if (result == MatchPlayer.Result.DRAW) {
                results[i] = 0.5;
            } else {
                results[i] = 0.0;
            }
        }
        return results;
    }

    @Override
    @Transactional
    public List<MatchDTO> findAllMatchByTournament(Long tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));
        return MatchMapper.toDTOList(matches);
    }

    @Override
    @Transactional
    public List<MatchDTO> findAllMatchByTournamentRound(Long tournamentId, Integer roundNumber) {
        matchRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament with id = " + tournamentId + " does not exist."));

        List<Match> matches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, roundNumber)
                .orElseThrow(() -> new TournamentRoundNotFoundException("Tournament with id = " + tournamentId + " does not have round = " + roundNumber));

        return MatchMapper.toDTOList(matches);
    }
}