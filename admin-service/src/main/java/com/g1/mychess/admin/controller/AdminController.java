package com.g1.mychess.admin.controller;

import com.g1.mychess.admin.dto.BlacklistDTO;
import com.g1.mychess.admin.dto.UserDTO;
import com.g1.mychess.admin.dto.WhitelistDTO;
import com.g1.mychess.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getAdminByUsername(@PathVariable String username) {
        UserDTO userDTO = adminService.findAdminByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/blacklist")
    public ResponseEntity<String> blacklistPlayer(@RequestBody @Valid BlacklistDTO blacklistDTO, HttpServletRequest request) {
        adminService.blacklistPlayer(blacklistDTO, request);
        return ResponseEntity.ok("Player blacklisted for " + blacklistDTO.getBanDuration() + " hours.");
    }

    @PostMapping("/whitelist")
    public ResponseEntity<String> whitelistPlayer(@RequestBody @Valid WhitelistDTO whitelistDTO, HttpServletRequest request) {
        adminService.whitelistPlayer(whitelistDTO, request);
        return ResponseEntity.ok("Player whitelisted successfully.");
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}
