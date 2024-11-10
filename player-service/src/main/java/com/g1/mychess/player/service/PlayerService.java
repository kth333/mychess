package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerService {

    ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO registerRequestDTO);

    void updatePlayerPassword(Long playerId, String newPassword);

    UserDTO findPlayerByUsername(String username);

    UserDTO findPlayerById(Long playerId);

    UserDTO findPlayerByEmail(String email);

    PlayerDTO getPlayerWithRatingDetails(Long playerId);

    void blacklistPlayer(Long playerId);

    void whitelistPlayer(Long playerId);

    AdminPlayerDTO getPlayerDetailsForAdmin(Long playerId);
}