package com.g1.mychess.player.controller;

import com.g1.mychess.player.dto.RegisterRequestDTO;
import com.g1.mychess.player.dto.UpdatePasswordRequestDTO;
import com.g1.mychess.player.dto.PlayerCreationResponseDTO;
import com.g1.mychess.player.dto.UserDTO;
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

    @PostMapping
    public ResponseEntity<PlayerCreationResponseDTO> createPlayer(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return playerService.createPlayer(registerRequestDTO);
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
}