package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.LeaderboardProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.dto.PlayerProfileUpdateDTO;
import com.g1.mychess.player.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.player.dto.PlayerRatingHistoryDTO;
import com.g1.mychess.player.service.PlayerRatingHistoryService;
import com.g1.mychess.player.service.ProfileService;
import com.g1.mychess.player.util.JwtUtil;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final PlayerRatingHistoryService playerRatingHistoryService;
    private final JwtUtil jwtUtil;

    public ProfileController(ProfileService profileService, PlayerRatingHistoryService playerRatingHistoryService, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.playerRatingHistoryService = playerRatingHistoryService;
        this.jwtUtil = jwtUtil;
    }

    // Fetch profile by playerId for any user
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerProfileDTO> getProfileById(@PathVariable Long playerId) {
        return ResponseEntity.ok(profileService.getPlayerProfile(playerId));
    }

    @PostMapping("/rating")
    public ResponseEntity<Void> updateProfileRating(@Valid @RequestBody PlayerRatingUpdateDTO ratingUpdateDTO) {
        profileService.updateProfileRating(ratingUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<String> updatePlayerProfile(@Valid @PathVariable Long playerId, @RequestBody PlayerProfileUpdateDTO profileUpdateDTO) {
        return profileService.updatePlayerProfile(playerId, profileUpdateDTO);
    }

    @GetMapping("/rating-history/{playerId}")
    public ResponseEntity<List<PlayerRatingHistoryDTO>> getPlayerRatingHistory(@Valid @PathVariable Long playerId) {
        return playerRatingHistoryService.getPlayerRatingHistory(playerId);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardProfileDTO>> getLeaderboard() {
        return ResponseEntity.ok(profileService.getLeaderboard());
    }

}
