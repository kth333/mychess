package com.g1.mychess.user.service;

import com.g1.mychess.user.dto.RegisterRequestDTO;
import com.g1.mychess.user.dto.UserCreationResponseDTO;
import com.g1.mychess.user.dto.UserDTO;
import com.g1.mychess.user.model.Player;
import com.g1.mychess.user.model.User;
import com.g1.mychess.user.repository.PlayerRepository;
import com.g1.mychess.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final PlayerRepository playerRepository;
    
    private final UserRepository userRepository;

    public UserService(PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserCreationResponseDTO> createUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserCreationResponseDTO(null, "Username already exists"));
        }

        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserCreationResponseDTO(null, "Email already exists"));
        }

        Player newPlayer = new Player();
        newPlayer.setUsername(registerRequestDTO.getUsername());
        newPlayer.setPassword(registerRequestDTO.getPassword());
        newPlayer.setEmail(registerRequestDTO.getEmail());
        playerRepository.save(newPlayer);

        return ResponseEntity.ok(new UserCreationResponseDTO(newPlayer.getId(), "User created successfully"));
    }

    public Long findUserIdByUsername(String username) {
        // Use the UserRepository to find the User entity by username
        return userRepository.findByUsername(username)
                .map(User::getId)  // If the user is found, map it to the ID
                .orElse(null);  // If the user is not found, return null
    }

    public UserDTO findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // Convert User to UserDTO
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
    }
}
