package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.PlayerProfileDTO;
import com.g1.mychess.player.service.ProfileService;
import com.g1.mychess.player.util.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    public ProfileController(ProfileService profileService, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('PLAYER')")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<PlayerProfileDTO> getPlayerProfile(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        Long userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(profileService.getPlayerProfile(userId));
    }
}
