package com.g1.mychess.tournament.mapper;

import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.model.TournamentPlayer;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A utility class to map between {@link Tournament} entities and their corresponding {@link TournamentDTO}
 * objects, as well as {@link TournamentPlayer} entities and their {@link TournamentPlayerDTO} counterparts.
 */
public class TournamentMapper {

    /**
     * Converts a {@link Tournament} entity to a {@link TournamentDTO}.
     *
     * @param tournament the tournament entity to convert
     * @return the corresponding {@link TournamentDTO}
     */
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

    /**
     * Converts a {@link TournamentPlayer} entity to a {@link TournamentPlayerDTO}.
     *
     * @param player the tournament player entity to convert
     * @return the corresponding {@link TournamentPlayerDTO}
     */
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

    /**
     * Converts a {@link TournamentDTO} to a {@link Tournament} entity.
     *
     * @param dto the DTO to convert
     * @return the corresponding {@link Tournament} entity
     */
    public static Tournament toEntity(TournamentDTO dto) {
        Tournament tournament = new Tournament();
        updateEntityFromDTO(tournament, dto);
        return tournament;
    }

    /**
     * Updates an existing {@link Tournament} entity with data from a {@link TournamentDTO}.
     *
     * @param tournament the tournament entity to update
     * @param dto        the DTO containing the data to update
     */
    public static void updateEntityFromDTO(Tournament tournament, TournamentDTO dto) {
        tournament.setName(dto.getName());
        tournament.setDescription(dto.getDescription());
        tournament.setMaxPlayers(dto.getMaxPlayers());
        tournament.setStartDateTime(dto.getStartDateTime());
        tournament.setEndDateTime(dto.getEndDateTime());
        tournament.setRegistrationStartDate(dto.getRegistrationStartDate());
        tournament.setRegistrationEndDate(dto.getRegistrationEndDate());
        tournament.setFormat(Tournament.TournamentFormat.valueOf(dto.getFormat()));
        tournament.setStatus(Tournament.TournamentStatus.valueOf(dto.getStatus()));
        tournament.setMinRating(dto.getMinRating());
        tournament.setMaxRating(dto.getMaxRating());
        tournament.setAffectsRating(dto.isAffectsRating());
        tournament.setMinAge(dto.getMinAge());
        tournament.setMaxAge(dto.getMaxAge());
        tournament.setRequiredGender(dto.getRequiredGender());
        tournament.setCountry(dto.getCountry());
        tournament.setRegion(dto.getRegion());
        tournament.setCity(dto.getCity());
        tournament.setAddress(dto.getAddress());
        tournament.setMaxRounds(dto.getMaxRounds());
        tournament.setTimeControlSetting(dto.getTimeControlSetting());
    }
}
