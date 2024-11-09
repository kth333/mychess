package com.g1.mychess.tournament.mapper;

import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TournamentPlayer;

import java.util.Set;
import java.util.stream.Collectors;

public class TournamentMapper {
    public static TournamentDTO toDTO(Tournament tournament) {
        Set<TournamentPlayerDTO> participantDTOs = tournament.getParticipants().stream()
                .map(TournamentMapper::toTournamentPlayerDTO)
                .collect(Collectors.toSet());

        return new TournamentDTO(
                tournament.getId(),
                tournament.getAdminId(),
                tournament.getName(),
                tournament.getDescription(),
                tournament.getMaxPlayers(),
                tournament.getStartDateTime(),
                tournament.getEndDateTime(),
                tournament.getRegistrationStartDate(),
                tournament.getRegistrationEndDate(),
                tournament.getFormat().name(),
                tournament.getStatus().name(),
                tournament.getMinRating(),
                tournament.getMaxRating(),
                tournament.isAffectsRating(),
                tournament.getMinAge(),
                tournament.getMaxAge(),
                tournament.getRequiredGender(),
                tournament.getCountry(),
                tournament.getRegion(),
                tournament.getCity(),
                tournament.getAddress(),
                tournament.getCurrentRound(),
                tournament.getMaxRounds(),
                participantDTOs,
                tournament.getTimeControlSetting()
        );
    }

    public static TournamentPlayerDTO toTournamentPlayerDTO(TournamentPlayer player) {
        return new TournamentPlayerDTO(
                player.getTournament().getId(),
                player.getPlayerId(),
                player.getSignUpDateTime(),
                player.getGlickoRating(),
                player.getRatingDeviation(),
                player.getVolatility(),
                player.getPoints(),
                player.getRoundsPlayed(),
                player.getStatus().name()
        );
    }
}