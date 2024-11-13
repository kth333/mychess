package com.g1.mychess.admin.controller;

import com.g1.mychess.admin.dto.BlacklistDTO;
import com.g1.mychess.admin.dto.UserDTO;
import com.g1.mychess.admin.dto.WhitelistDTO;
import com.g1.mychess.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AdminController handles operations related to the admin such as blacklisting and whitelisting players,
 * retrieving admin details by username, and providing a health check for the service.
 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * Constructor for injecting the AdminService into the controller.
     *
     * @param adminService the AdminService to handle business logic
     */
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Retrieves the details of an admin by their username.
     *
     * @param username the username of the admin to fetch
     * @return ResponseEntity containing the UserDTO of the admin
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getAdminByUsername(@PathVariable String username) {
        UserDTO userDTO = adminService.findAdminByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Blacklists a player by providing their details and the ban duration.
     *
     * @param blacklistDTO contains the player's information and ban duration
     * @param request the HTTP request containing session or context information
     * @return ResponseEntity indicating the success of the operation with the ban duration
     */
    @PostMapping("/blacklists")
    public ResponseEntity<String> blacklistPlayer(@RequestBody @Valid BlacklistDTO blacklistDTO, HttpServletRequest request) {
        adminService.blacklistPlayer(blacklistDTO, request);
        return ResponseEntity.ok("Player blacklisted for " + blacklistDTO.getBanDuration() + " hours.");
    }

    /**
     * Whitelists a player based on the provided details.
     *
     * @param whitelistDTO contains the player's details to be whitelisted
     * @param request the HTTP request for session context
     * @return ResponseEntity indicating success
     */
    @PostMapping("/whitelists")
    public ResponseEntity<String> whitelistPlayer(@RequestBody @Valid WhitelistDTO whitelistDTO, HttpServletRequest request) {
        adminService.whitelistPlayer(whitelistDTO, request);
        return ResponseEntity.ok("Player whitelisted successfully.");
    }

    /**
     * Provides a health check endpoint to verify the service is up and running.
     *
     * @return ResponseEntity containing the status message of the service health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}