package com.g1.mychess.user.service;

import com.g1.mychess.user.dto.RegisterRequestDTO;
import com.g1.mychess.user.model.Player;
import com.g1.mychess.user.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PlayerRepository playerRepository;

    public boolean createUser(RegisterRequestDTO registerRequestDTO) {
        // Check if the user already exists by email or username
        if (playerRepository.existsByEmail(registerRequestDTO.getEmail()) || playerRepository.existsByUsername(registerRequestDTO.getUsername())) {
            return false; // User already exists
        }

        // Create new Player entity
        Player newPlayer = new Player();
        newPlayer.setUsername(registerRequestDTO.getUsername());
        newPlayer.setEmail(registerRequestDTO.getEmail());
        newPlayer.setPassword(registerRequestDTO.getPassword()); // Hash password in real implementation

        // Save to database
        playerRepository.save(newPlayer);
        return true;
    }
}
