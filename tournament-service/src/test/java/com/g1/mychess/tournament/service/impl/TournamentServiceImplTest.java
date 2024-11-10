package com.g1.mychess.tournament.service.impl;

import com.g1.mychess.tournament.client.MatchServiceClient;
import com.g1.mychess.tournament.client.PlayerServiceClient;
import com.g1.mychess.tournament.dto.MatchmakingDTO;
import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.dto.TournamentDTO;
import com.g1.mychess.tournament.exception.*;
import com.g1.mychess.tournament.mapper.TournamentMapper;
import com.g1.mychess.tournament.model.TimeControlSetting;
import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.tournament.repository.TournamentPlayerRepository;
import com.g1.mychess.tournament.repository.TournamentRepository;
import com.g1.mychess.tournament.service.AuthenticationService;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TournamentServiceImplTest {

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentPlayerRepository tournamentPlayerRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PlayerServiceClient playerServiceClient;

    @Mock
    private MatchServiceClient matchServiceClient;

    @Mock
    private HttpServletRequest mockRequest;

    @Test
    public void testCreateTournament_Success() {
        // Arrange
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setName("Test Tournament");
        tournamentDTO.setDescription("A test tournament");
        tournamentDTO.setMaxPlayers(16);
        tournamentDTO.setStartDateTime(LocalDateTime.now().plusDays(1));
        tournamentDTO.setEndDateTime(LocalDateTime.now().plusDays(2));
        tournamentDTO.setRegistrationStartDate(LocalDateTime.now());
        tournamentDTO.setRegistrationEndDate(LocalDateTime.now().plusHours(12));
        tournamentDTO.setFormat("SWISS");
        tournamentDTO.setStatus("UPCOMING");
        tournamentDTO.setMinRating(1000);
        tournamentDTO.setMaxRating(2000);
        tournamentDTO.setAffectsRating(true);
        tournamentDTO.setMinAge(18);
        tournamentDTO.setMaxAge(60);
        tournamentDTO.setRequiredGender("ANY");
        tournamentDTO.setCountry("USA");
        tournamentDTO.setRegion("CA");
        tournamentDTO.setCity("San Francisco");
        tournamentDTO.setAddress("123 Chess St.");
        tournamentDTO.setMaxRounds(4);

        // Adjusting for TimeControlSetting
        TimeControlSetting timeControlSetting = new TimeControlSetting(15, 10); // 15 minutes base time with 10 seconds increment
        tournamentDTO.setTimeControlSetting(timeControlSetting);

        when(tournamentRepository.findByName(tournamentDTO.getName())).thenReturn(Optional.empty());
        when(authenticationService.getUserIdFromRequest(mockRequest)).thenReturn(1L);

        ArgumentCaptor<Tournament> tournamentCaptor = ArgumentCaptor.forClass(Tournament.class);
        when(tournamentRepository.save(tournamentCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<TournamentDTO> response = tournamentService.createTournament(tournamentDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        TournamentDTO responseBody = response.getBody();

        assertEquals("Test Tournament", responseBody.getName());

        Tournament savedTournament = tournamentCaptor.getValue();
        assertEquals(1L, savedTournament.getAdminId());
        assertEquals("Test Tournament", savedTournament.getName());
        assertEquals(timeControlSetting, savedTournament.getTimeControlSetting());
    }

    @Test
    void testSignUpToTournament_Success() {
        // Arrange
        long tournamentId = 1L;
        long playerId = 2L;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setParticipants(new HashSet<>());
        tournament.setMinRating(1000); // Add this line
        tournament.setMaxRating(2000); // Add this line

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)).thenReturn(false);

        PlayerDTO playerDTO = new PlayerDTO(
                playerId,
                false,
                "playerUsername",
                25,
                "MALE",
                1500.0,
                200.0,
                0.06
        );

        when(playerServiceClient.getPlayerDetails(playerId)).thenReturn(playerDTO);

        // Act
        ResponseEntity<String> response = tournamentService.signUpToTournament(tournamentId, playerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Player successfully signed up to the tournament", response.getBody());
        verify(tournamentRepository).save(any(Tournament.class));
    }

    @Test
    void testSignUpToTournament_PlayerEligibilityCheck_FailsDueToBlacklisted() {
        // Arrange
        long tournamentId = 1L;
        long playerId = 2L;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setParticipants(new HashSet<>());

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(tournamentPlayerRepository.existsByTournamentIdAndPlayerId(tournamentId, playerId)).thenReturn(false);

        // Player is blacklisted
        PlayerDTO playerDTO = new PlayerDTO(
                playerId,
                true, // isBlacklisted
                "playerUsername",
                25,
                "MALE",
                1500.0,
                200.0,
                0.06
        );

        when(playerServiceClient.getPlayerDetails(playerId)).thenReturn(playerDTO);

        // Act & Assert
        PlayerBlacklistedException exception = assertThrows(PlayerBlacklistedException.class, () ->
                tournamentService.signUpToTournament(tournamentId, playerId)
        );
        assertEquals("Player is blacklisted from participating in tournaments.", exception.getMessage());
    }

    // Example for testCompleteTournament_Success
    @Test
    void testCompleteTournament_Success() {
        // Arrange
        long tournamentId = 1L;
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        tournament.setAdminId(1L);
        tournament.setStatus(Tournament.TournamentStatus.ONGOING);
        tournament.setParticipants(new HashSet<>());

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        when(authenticationService.getUserIdFromRequest(mockRequest)).thenReturn(1L);
        when(authenticationService.extractJwtToken(mockRequest)).thenReturn("mock-jwt-token");

        // Act
        ResponseEntity<String> response = tournamentService.completeTournament(tournamentId, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tournament completed successfully.", response.getBody());
        assertEquals(Tournament.TournamentStatus.COMPLETED, tournament.getStatus());
        verify(matchServiceClient).finalizeTournament(any(MatchmakingDTO.class), eq("mock-jwt-token"));
    }

    @Test
   void test_FindTournamentByName_Found() {
        String tournamentName = "Tournament";
        Tournament tournament = mock(Tournament.class);
        TournamentDTO tournamentDTO = mock(TournamentDTO.class);

        when(tournamentRepository.findByName(tournamentName)).thenReturn(Optional.of(tournament));
        // MockedStatic was generated using ChatGPT 4.0
        MockedStatic<TournamentMapper> mockedMapper = mockStatic(TournamentMapper.class);
        mockedMapper.when(() -> TournamentMapper.toDTO(tournament)).thenReturn(tournamentDTO);
        ResponseEntity<TournamentDTO> result = tournamentService.findTournamentByName(tournamentName);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        mockedMapper.close();
    }

    @Test
    void test_FindTournamentByID_Found(){
        long tournamentId = 1l;
        Tournament tournament = mock(Tournament.class);
        TournamentDTO tournamentDTO = mock(TournamentDTO.class);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        MockedStatic<TournamentMapper> mockedMapper = mockStatic(TournamentMapper.class);
        mockedMapper.when(() -> TournamentMapper.toDTO(tournament)).thenReturn(tournamentDTO);

        ResponseEntity<TournamentDTO> result = tournamentService.findTournamentById(tournamentId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void test_FindTournamentByID_NotExist(){
        long tournamentId = 1L;

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                tournamentService.findTournamentById(tournamentId)
        );
        assertEquals("Tournament not found with tournament id: " + tournamentId, exception.getMessage());
    }

    @Test
    void test_FindTournamentByName_NotExist(){
        String tournamentName = "Tournament";

        when(tournamentRepository.findByName(tournamentName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                tournamentService.findTournamentByName(tournamentName)
        );
        assertEquals("Tournament not found with name: " + tournamentName, exception.getMessage());
    }

    @Test
    void testGetAllTournaments_Success() {

    }

    @Test
    public void testUpdateTournament_Success() {
        // Arrange
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(1L);
        tournamentDTO.setName("Updated Tournament");
        tournamentDTO.setFormat("SWISS");
        tournamentDTO.setStatus(Tournament.TournamentStatus.UPCOMING.name());

        TimeControlSetting timeControlSetting = new TimeControlSetting(30, 0); // 30 minutes base time, no increment
        tournamentDTO.setTimeControlSetting(timeControlSetting);

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setAdminId(1L);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(authenticationService.getUserIdFromRequest(mockRequest)).thenReturn(1L);
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<TournamentDTO> response = tournamentService.updateTournament(tournamentDTO, mockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Tournament", response.getBody().getName());
        assertEquals(timeControlSetting, response.getBody().getTimeControlSetting());
    }

   // ... (Include other test methods, adjusting them similarly)
   @Test
   public void testUpdateTournament_NotExist() {
        // Arrange
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(1L);
        tournamentDTO.setName("Updated Tournament");
        tournamentDTO.setFormat("SWISS");
        tournamentDTO.setStatus(Tournament.TournamentStatus.UPCOMING.name());

        TimeControlSetting timeControlSetting = new TimeControlSetting(30, 0); // 30 minutes base time, no increment
        tournamentDTO.setTimeControlSetting(timeControlSetting);

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setAdminId(1L);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                tournamentService.updateTournament(tournamentDTO, mockRequest));

        assertEquals("Tournament not found with ID: " + 1L, exception.getMessage());
   }

    @Test
    public void testUpdateTournament_Forbidden_NotAdmin() {
    long adminId = 999L;
    TournamentDTO tournamentDTO = new TournamentDTO();
    tournamentDTO.setId(1L);
    tournamentDTO.setAdminId(adminId);

    long userId = 1L;

    HttpServletRequest request = mock(HttpServletRequest.class);
    Tournament tournament = mock(Tournament.class);

    when(tournamentRepository.findById(tournamentDTO.getId())).thenReturn(Optional.of(tournament));
    when(authenticationService.getUserIdFromRequest(request)).thenReturn(userId);
    when(tournament.getAdminId()).thenReturn(adminId);

    Exception exception = assertThrows(UnauthorizedActionException.class, () ->
            tournamentService.updateTournament(tournamentDTO, request)
    );

    assertEquals("Only the tournament admin can update the tournament.", exception.getMessage());
    }



}
