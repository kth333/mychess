package com.g1.mychess.user.controller;

import com.g1.mychess.user.dto.RegisterRequestDTO;
import com.g1.mychess.user.dto.UserCreationResponseDTO;
import com.g1.mychess.user.dto.UserDTO;
import com.g1.mychess.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserCreationResponseDTO> createUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return userService.createUser(registerRequestDTO);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO userDTO = userService.findUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }
}
