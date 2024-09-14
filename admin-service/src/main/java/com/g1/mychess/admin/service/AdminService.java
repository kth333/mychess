package com.g1.mychess.admin.service;

import com.g1.mychess.admin.dto.UserDTO;
import com.g1.mychess.admin.model.Admin;
import com.g1.mychess.admin.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.g1.mychess.admin.exception.AdminNotFoundException;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public UserDTO findAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with username: " + username));

        return new UserDTO(admin.getAdminId(), admin.getUsername(), admin.getPassword(), admin.getEmail(), admin.getRole());
    }
}