package com.g1.mychess.match.mapper;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.dto.TournamentResultsDTO;
import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatchMapper {

    public static MatchDTO toDTO(Match match) {
        return MatchDTO.fromEntity(match);
    }

    public static List<MatchDTO> toDTOList(List<Match> matches) {
        return matches.stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static TournamentResultsDTO toTournamentResultsDTO(List<MatchPlayer> matchPlayers) {
        // Create a map of unique player IDs and corresponding PlayerResultDTOs (no points summing)
        Map<Long, TournamentResultsDTO.PlayerResultDTO> uniquePlayerResults = matchPlayers.stream()
                .collect(Collectors.toMap(
                        MatchPlayer::getPlayerId,
                        mp -> new TournamentResultsDTO.PlayerResultDTO(mp.getPlayerId(), mp.getPoints()),
                        (existing, replacement) -> existing // Keep the first occurrence of each player ID
                ));

        // Convert map values to a list for the final DTO
        List<TournamentResultsDTO.PlayerResultDTO> playerResults = new ArrayList<>(uniquePlayerResults.values());

        // Assume tournamentId is derived from the first match's tournamentId in the list of MatchPlayers
        Long tournamentId = matchPlayers.isEmpty() ? null : matchPlayers.get(0).getMatch().getTournamentId();

        // Return the final TournamentResultsDTO with unique player results
        return new TournamentResultsDTO(tournamentId, playerResults);
    }



}