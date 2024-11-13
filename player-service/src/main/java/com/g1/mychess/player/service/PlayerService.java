package com.g1.mychess.player.service;

import com.g1.mychess.player.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerService {

    ResponseEntity<PlayerCreationResponseDTO> createPlayer(RegisterRequestDTO registerRequestDTO);

    void updatePlayerPassword(Long playerId, String newPassword);

    UserDTO findPlayerByUsername(String username);

    UserDTO findPlayerById(Long playerId);

    UserDTO findPlayerByEmail(String email);

    PlayerDTO getPlayerDetails(Long playerId);

    ResponseEntity<String> reportPlayer(ReportPlayerRequestDTO reportPlayerRequestDTO);

    void blacklistPlayer(Long playerId);

    void whitelistPlayer(Long playerId);

    Page<PlayerDTO> searchPlayers(String query, int page, int size);

    ResponseEntity<String> followPlayer(Long followerId, Long followedId);

    ResponseEntity<String> unfollowPlayer(Long followerId, Long followedId);

    Page<PlayerDTO> getFollowedPlayers(Long followerId, int page, int size);

    Page<PlayerDTO> getFollowers(Long playerId, int page, int size);
}