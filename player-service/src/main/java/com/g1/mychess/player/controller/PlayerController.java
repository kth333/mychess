package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.service.PlayerService;
import com.g1.mychess.player.util.JwtUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {

    private final PlayerService playerService;
    private final JwtUtil jwtUtil;

    public PlayerController(PlayerService playerService, JwtUtil jwtUtil) {
        this.playerService = playerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/")
    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return playerService.createPlayer(registerRequestDTO);
    }

    @PutMapping("/blacklist-status/{playerId}")
    public ResponseEntity<String> updateBlacklistStatus(@PathVariable @Positive Long playerId) {
        playerService.blacklistPlayer(playerId);
        return ResponseEntity.ok("Player has been blacklisted successfully.");
    }

    @PutMapping("/whitelist-status/{playerId}")
    public ResponseEntity<String> updateWhitelistStatus(@PathVariable @Positive Long playerId) {
        playerService.whitelistPlayer(playerId);
        return ResponseEntity.ok("Player has been whitelisted successfully.");
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getPlayerByUsername(@Valid @PathVariable String username) {
        UserDTO userDTO = playerService.findPlayerByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/playerId/{playerId}")
    public ResponseEntity<UserDTO> getPlayerById(@Valid @PathVariable Long playerId) {
        UserDTO userDTO = playerService.findPlayerById(playerId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getPlayerByEmail(@Valid @PathVariable String email) {
        UserDTO userDTO = playerService.findPlayerByEmail(email);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequestDTO updatePasswordRequest) {
        playerService.updatePlayerPassword(updatePasswordRequest.getPlayerId(), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }

    @GetMapping("/{playerId}/details")
    public ResponseEntity<PlayerDTO> getPlayerWithRatingDetails(@Valid @PathVariable Long playerId) {
        PlayerDTO playerDTO = playerService.getPlayerWithRatingDetails(playerId);
        return ResponseEntity.ok(playerDTO);
    }

    @GetMapping("/{playerId}/admin-details")
    public ResponseEntity<AdminPlayerDTO> getPlayerDetailsForAdmin(@Valid @PathVariable Long playerId) {
        AdminPlayerDTO adminPlayerDTO = playerService.getPlayerDetailsForAdmin(playerId);
        return ResponseEntity.ok(adminPlayerDTO);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}