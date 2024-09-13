package com.g1.mychess.tournament.service;

import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.exception.TournamentAlreadyExistsException;
import com.g1.mychess.tournament.repository.TournamentRepository;

@Service
public class TournamentService {
    
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO) {
        // Check if a tournament with the same name already exists
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
            throw new TournamentAlreadyExistsException("Tournament with the name " + tournamentDTO.getName() + " already exists.");
        }

        // Map TournamentDTO to Tournament entity
        Tournament tournament = new Tournament();
        tournament.setAdminId(tournamentDTO.getAdminId());
        tournament.setName(tournamentDTO.getName());
        tournament.setDescription(tournamentDTO.getDescription());
        tournament.setStartDateTime(tournamentDTO.getStartDateTime());
        tournament.setEndDateTime(tournamentDTO.getEndDateTime());
        tournament.setRegistrationStartDate(tournamentDTO.getRegistrationStartDate());
        tournament.setRegistrationEndDate(tournamentDTO.getRegistrationEndDate());
        tournament.setFormat(Tournament.TournamentFormat.valueOf(tournamentDTO.getFormat()));
        tournament.setStatus(Tournament.TournamentStatus.valueOf(tournamentDTO.getStatus()));
        tournament.setMinRating(tournamentDTO.getMinRating());
        tournament.setMaxRating(tournamentDTO.getMaxRating());
        tournament.setAffectsRating(tournamentDTO.isAffectsRating());
        tournament.setMinAge(tournamentDTO.getMinAge());
        tournament.setMaxAge(tournamentDTO.getMaxAge());
        tournament.setRequiredGender(tournamentDTO.getRequiredGender());
        tournament.setCountry(tournamentDTO.getCountry());
        tournament.setRegion(tournamentDTO.getRegion());
        tournament.setCity(tournamentDTO.getCity());
        tournament.setAddress(tournamentDTO.getAddress());

        // Save the tournament to the repository
        Tournament savedTournament = tournamentRepository.save(tournament);

        // Map the saved Tournament entity back to a TournamentDTO
        TournamentDTO savedTournamentDTO = new TournamentDTO(
                savedTournament.getId(),
                savedTournament.getAdminId(),
                savedTournament.getName(),
                savedTournament.getDescription(),
                savedTournament.getStartDateTime(),
                savedTournament.getEndDateTime(),
                savedTournament.getRegistrationStartDate(),
                savedTournament.getRegistrationEndDate(),
                savedTournament.getFormat().name(),
                savedTournament.getStatus().name(),
                savedTournament.getMinRating(),
                savedTournament.getMaxRating(),
                savedTournament.isAffectsRating(),
                savedTournament.getMinAge(),
                savedTournament.getMaxAge(),
                savedTournament.getRequiredGender(),
                savedTournament.getCountry(),
                savedTournament.getRegion(),
                savedTournament.getCity(),
                savedTournament.getAddress(),
                new HashSet<>()
        );

        // Return the saved tournament details
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTournamentDTO);
    }

    public TournamentDTO findTournamentByName(String name) {
        Tournament tournament = tournamentRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with name: " + name));
    
        // Convert Tournament to TournamentDTO
        return new TournamentDTO(
                tournament.getId(),
                tournament.getAdminId(),  // Assuming there is an admin relation in Tournament entity
                tournament.getName(),
                tournament.getDescription(),
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
                tournament.getParticipants().stream()
                    .map(participant -> new TournamentPlayerDTO(
                        participant.getId(),
                        participant.getTournament().getId(),  
                        participant.getPlayerId(),      
                        participant.getSignUpDateTime(),
                        participant.getPoints(),
                        participant.getRoundsPlayed(),
                        participant.getStatus().name()))
                    .collect(Collectors.toSet())
        );
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
