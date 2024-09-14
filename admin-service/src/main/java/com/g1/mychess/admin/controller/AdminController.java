package com.g1.mychess.admin.controller;

import com.g1.mychess.admin.dto.UserDTO;
import com.g1.mychess.admin.service.AdminService;
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
}
