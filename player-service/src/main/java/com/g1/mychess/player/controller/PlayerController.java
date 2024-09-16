package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/create")
    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return playerService.createPlayer(registerRequestDTO);
    }

    @PutMapping("/update-blacklist-status")
    public ResponseEntity<String> updateBlacklistStatus(@RequestParam Long playerId) {
        playerService.blacklistPlayer(playerId);  // Use the playerId directly
        return ResponseEntity.ok("Player has been blacklisted successfully.");
    }

    @PutMapping("/update-whitelist-status")
    public ResponseEntity<String> updateWhitelistStatus(@RequestParam Long playerId) {
        playerService.whitelistPlayer(playerId);  // Use the playerId directly
        return ResponseEntity.ok("Player has been whitelisted successfully.");
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getPlayerByUsername(@PathVariable String username) {
        UserDTO userDTO = playerService.findPlayerByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/playerId/{playerId}")
    public ResponseEntity<UserDTO> getPlayerById(@PathVariable Long playerId) {
        UserDTO userDTO = playerService.findPlayerById(playerId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getPlayerByEmail(@PathVariable String email) {
        UserDTO userDTO = playerService.findPlayerByEmail(email);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequestDTO updatePasswordRequest) {
        playerService.updatePlayerPassword(updatePasswordRequest.getPlayerId(), updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }

    @GetMapping("/{playerId}/details")
    public ResponseEntity<PlayerDTO> getPlayerWithRatingDetails(@PathVariable Long playerId) {
        PlayerDTO playerDTO = playerService.getPlayerWithRatingDetails(playerId);
        return ResponseEntity.ok(playerDTO);
    }

    @GetMapping("/{playerId}/admin-details")
    public ResponseEntity<AdminPlayerDTO> getPlayerDetailsForAdmin(@PathVariable Long playerId) {
        AdminPlayerDTO adminPlayerDTO = playerService.getPlayerDetailsForAdmin(playerId);
        return ResponseEntity.ok(adminPlayerDTO);
    }
}