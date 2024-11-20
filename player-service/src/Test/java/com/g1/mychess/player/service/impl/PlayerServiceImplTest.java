//package com.g1.mychess.player.service.impl;
//
//import com.g1.mychess.player.service.impl.PlayerServiceImpl;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import com.g1.mychess.player.dto.*;
//import com.g1.mychess.player.exception.PlayerNotFoundException;
//import com.g1.mychess.player.mapper.PlayerMapper;
//import com.g1.mychess.player.model.Player;
//import com.g1.mychess.player.model.PlayerRatingHistory;
//import com.g1.mychess.player.model.Profile;
//import com.g1.mychess.player.repository.PlayerRatingHistoryRepository;
//import com.g1.mychess.player.repository.PlayerRepository;
//import com.g1.mychess.player.repository.ProfileRepository;
//import com.g1.mychess.player.service.PlayerService;
//import jakarta.transaction.Transactional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//import static org.springframework.util.ClassUtils.isPresent;
//
//
//@SpringBootTest
//public class PlayerServiceImplTest {
//    @MockBean
//    PlayerRepository playerRepository;
//    @MockBean
//    ProfileRepository profileRepository;
//    @MockBean
//    PlayerRatingHistoryRepository playerRatingHistoryRepository;
//    @InjectMocks
//    PlayerServiceImpl playerService;
//
//    //    private Profile getProfileByPlayerId(Long playerId) {
////        return profileRepository.findById(playerId)
////                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
////
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void createPlayer() {
//
//        String username = "username";
//        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
//        registerRequestDTO.setUsername(username); // Set the username directly
//
//        Player player = mock(Player.class);
//        when(playerRepository.findByUsername(username)).thenReturn(Optional.of(player));
//
//        ResponseEntity<PlayerCreationResponseDTO> result = playerService.createPlayer(registerRequestDTO);
//        assertEquals(result.getStatusCode(), HttpStatus.CONFLICT);
//        verify(playerRepository.findByUsername(username));
//    }
//}