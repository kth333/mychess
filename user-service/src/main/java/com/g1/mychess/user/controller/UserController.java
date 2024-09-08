package com.g1.mychess.user.controller;

import com.g1.mychess.user.dto.RegisterRequestDTO;
import com.g1.mychess.user.dto.UserDTO;
import com.g1.mychess.user.model.User;
import com.g1.mychess.user.service.UserService;
import com.g1.mychess.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByUsername(registerRequestDTO.getUsername());
        if (existingUser.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);  // Return 409 with message
        }

        // Delegate user creation to the userService

        // Return user creation response from the service
        return userService.createUser(registerRequestDTO);
    }

    @GetMapping("/userId/{username}")
    public ResponseEntity<Long> getUserIdByUsername(@PathVariable("username") String username) {
        Long userId = userService.findUserIdByUsername(username);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO userDTO = userService.findUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }
}
