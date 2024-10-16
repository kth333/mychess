package com.g1.mychess.admin.service;

import com.g1.mychess.admin.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;

public interface AdminService {

    UserDTO findAdminByUsername(String username);

    void blacklistPlayer(BlacklistDTO blacklistDTO, HttpServletRequest request);

    void whitelistPlayer(WhitelistDTO whitelistDTO, HttpServletRequest request);

    @Scheduled(fixedRate = 3600000)  // Runs every hour (3600000 ms)
    void autoWhitelistExpiredBans();
}