package com.g1.mychess.tournament.service;

import java.util.stream.Collectors;

import com.g1.mychess.tournament.util.JwtUtil;
import jakarta.transaction.Transactional;

import org.apache.el.stream.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.dto.TournamentPlayerDTO;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.exception.TournamentAlreadyExistsException;
import com.g1.mychess.tournament.repository.TournamentRepository;

@Service
public class TournamentService {
    
    private final TournamentRepository tournamentRepository;

    private final JwtUtil jwtUtil;

    public TournamentService(TournamentRepository tournamentRepository, JwtUtil jwtUtil) {
        this.tournamentRepository = tournamentRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public ResponseEntity<TournamentDTO> createTournament(TournamentDTO tournamentDTO, HttpServletRequest request) {
        // Check if a tournament with the same name already exists
        if (tournamentRepository.findByName(tournamentDTO.getName()).isPresent()) {
            throw new TournamentAlreadyExistsException("Tournament with the name " + tournamentDTO.getName() + " already exists.");
        }
        String jwtToken = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);

        // Map TournamentDTO to Tournament entity
        Tournament tournament = new Tournament();
        tournament.setAdminId(userId);
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
        tournament.setParticipants(new HashSet<>());

        // Save the tournament to the repository
        Tournament savedTournament = tournamentRepository.save(tournament);
       
        // Map the saved Tournament entity back to a TournamentDTO
        TournamentDTO savedTournamentDTO = convertToDTO(savedTournament);

        // Return the saved tournament details
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTournamentDTO);
    }

    public ResponseEntity<TournamentDTO> findTournamentByName(String name) {
        Tournament tournament = tournamentRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with name: " + name));
    
        // Convert Tournament to TournamentDTO
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(tournament));
    }

    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        List<TournamentDTO> tournamentDTOs = tournaments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK).body(tournamentDTOs);
    }

    public ResponseEntity<TournamentDTO> updateTournament(TournamentDTO tournamentDTO) {
        // Find the tournament by its ID
        Tournament tournament = tournamentRepository.findById(tournamentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentDTO.getId()));

        // Update the tournament fields
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

        // Save the updated tournament to the repository
        Tournament updatedTournament = tournamentRepository.save(tournament);

        // Convert the updated Tournament entity back to a TournamentDTO
        TournamentDTO updatedTournamentDTO = convertToDTO(updatedTournament);

        // Return the updated tournament details
        return ResponseEntity.status(HttpStatus.OK).body(updatedTournamentDTO);
    }
    
    public ResponseEntity<String> signUpToTournament(Long tournamentId, Long userId) {
        // Fetch the tournament from the repository
        Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentId);
        if (!optionalTournament.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found");
        }
        Tournament tournament = optionalTournament.get();
    
        // Fetch the user from the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = optionalUser.get();
    
        // Check if the user meets the tournament's requirements
        if (user.getRating() < tournament.getMinRating() || user.getRating() > tournament.getMaxRating()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not meet the rating requirements");
        }
        if (user.getAge() < tournament.getMinAge() || user.getAge() > tournament.getMaxAge()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not meet the age requirements");
        }
        if (tournament.getRequiredGender() != null && !tournament.getRequiredGender().equals(user.getGender())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not meet the gender requirements");
        }
    
        // Add the user to the tournament's list of participants
        tournament.getParticipants().add(user);
    
        // Save the updated tournament to the repository
        tournamentRepository.save(tournament);
    
        // Return a success response
        return ResponseEntity.status(HttpStatus.OK).body("User successfully signed up to the tournament");
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
