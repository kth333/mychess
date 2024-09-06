package com.g1.mychess.user.controller;

import com.g1.mychess.user.dto.RegisterRequestDTO;
import com.g1.mychess.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        boolean userCreated = userService.createUser(registerRequestDTO);

        if (userCreated) {
            return ResponseEntity.ok("User created successfully");
        } else {
            return ResponseEntity.status(400).body("User already exists");
        }
    }
}
