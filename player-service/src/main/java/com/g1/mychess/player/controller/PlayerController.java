package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.*;
import com.g1.mychess.player.service.PlayerService;
import com.g1.mychess.player.util.JwtUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

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
    public ResponseEntity<PlayerDTO> getPlayerWithDetails(@Valid @PathVariable Long playerId) {
        PlayerDTO playerDTO = playerService.getPlayerDetails(playerId);
        return ResponseEntity.ok(playerDTO);
    }

    @PostMapping("/reports")
    public ResponseEntity<String> reportPlayer(@RequestBody ReportPlayerRequestDTO reportPlayerRequestDTO) {
        return playerService.reportPlayer(reportPlayerRequestDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PlayerDTO>> searchPlayers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PlayerDTO> result = playerService.searchPlayers(query, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{followerId}/follow/{followedId}")
    public ResponseEntity<String> followPlayer(@PathVariable Long followerId, @PathVariable Long followedId) {
        return playerService.followPlayer(followerId, followedId);
    }

    @DeleteMapping("/{followerId}/unfollow/{followedId}")
    public ResponseEntity<String> unfollowPlayer(@PathVariable Long followerId, @PathVariable Long followedId) {
        return playerService.unfollowPlayer(followerId, followedId);
    }

    @GetMapping("/{playerId}/followers")
    public ResponseEntity<Page<PlayerDTO>> getFollowers(
            @PathVariable Long playerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PlayerDTO> followers = playerService.getFollowers(playerId, page, size);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{playerId}/following")
    public ResponseEntity<Page<PlayerDTO>> getFollowing(
            @PathVariable Long playerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PlayerDTO> following = playerService.getFollowedPlayers(playerId, page, size);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}