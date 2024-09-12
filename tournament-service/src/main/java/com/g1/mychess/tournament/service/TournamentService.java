package com.g1.mychess.tournament.service;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.repository.TournamentRepository;

public class TournamentService {
    
    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO){
        // if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
        //     return ResponseEntity.status(HttpStatus.CONFLICT)
        //             .body(new TournamentDTO(null, "Name already exists"));
        // }
        return null;
    }

    public TournamentDTO convertToDTO(Tournament tournament) {
        // Convert each TournamentPlayer to TournamentPlayerDTO using the correct fields
        Set<TournamentPlayerDTO> participantDTOs = tournament.getParticipants().stream()
            .map(player -> new TournamentPlayerDTO(
                player.getId(),
                player.getTournament().getId(), // Assuming you want to include the tournament ID
                player.getPlayerId(), 
                player.getSignUpDateTime(),
                player.getPoints(),
                player.getRoundsPlayed(),
                player.getStatus().name() // Convert enum to string
            ))
            .collect(Collectors.toSet());
    
        // Map the Tournament fields to TournamentDTO fields
        return new TournamentDTO(
            tournament.getId(),
            tournament.getAdminId(),
            tournament.getName(),
            tournament.getDescription(),
            tournament.getStartDateTime(),
            tournament.getEndDateTime(),
            tournament.getRegistrationStartDate(),
            tournament.getRegistrationEndDate(),
            tournament.getFormat().name(), // Convert enum to string
            tournament.getStatus().name(), // Convert enum to string
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
            participantDTOs // Pass the converted set of participant DTOs
        );
    }
    

}
