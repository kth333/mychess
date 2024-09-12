package com.g1.mychess.user.service;

import com.g1.mychess.user.dto.RegisterRequestDTO;
import com.g1.mychess.user.dto.UserCreationResponseDTO;
import com.g1.mychess.user.dto.UserDTO;
import com.g1.mychess.user.model.Player;
import com.g1.mychess.user.model.User;
import com.g1.mychess.user.repository.PlayerRepository;
import com.g1.mychess.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.g1.mychess.user.exception.UserNotFoundException;

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

    public void updateUserPassword(Long userId, String newPassword) {
        // Find the user by their ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        // Set the new hashed password
        user.setPassword(newPassword);

        // Save the updated user entity to the database
        userRepository.save(user);
    }

    public UserDTO findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // Convert User to UserDTO
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
    }

    public UserDTO findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with user Id: " + userId));

        // Convert User to UserDTO
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
    }

    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // Convert User to UserDTO
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
    }
}
