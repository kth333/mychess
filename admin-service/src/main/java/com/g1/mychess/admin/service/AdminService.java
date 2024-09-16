package com.g1.mychess.admin.service;

import com.g1.mychess.admin.dto.*;
import com.g1.mychess.admin.exception.InvalidBlacklistOperationException;
import com.g1.mychess.admin.model.Admin;
import com.g1.mychess.admin.model.Blacklist;
import com.g1.mychess.admin.repository.AdminRepository;
import com.g1.mychess.admin.repository.BlacklistRepository;
import com.g1.mychess.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final BlacklistRepository blacklistRepository;
    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    public AdminService(AdminRepository adminRepository, BlacklistRepository blacklistRepository, WebClient.Builder webClientBuilder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.blacklistRepository = blacklistRepository;
        this.webClientBuilder = webClientBuilder;
        this.jwtUtil = jwtUtil;
    }

    public UserDTO findAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with username: " + username));

        return new UserDTO(admin.getAdminId(), admin.getUsername(), admin.getPassword(), admin.getEmail(), admin.getRole());
    }

    @Transactional
    public void blacklistPlayer(BlacklistDTO blacklistDTO, HttpServletRequest request) {
        // Fetch player details from the player service
        AdminPlayerDTO adminPlayerDTO = fetchPlayerDetails(blacklistDTO.getPlayerId());

        if (adminPlayerDTO.isBlacklisted()) {
            throw new InvalidBlacklistOperationException(
                    "Player with ID " + blacklistDTO.getPlayerId() + " is already blacklisted.");
        }

        blacklistDTO.setUsername(adminPlayerDTO.getUsername());
        blacklistDTO.setEmail(adminPlayerDTO.getEmail());

        Blacklist blacklist = blacklistRepository.findByPlayerId(blacklistDTO.getPlayerId())
                .orElse(new Blacklist());

        String jwtToken = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);  // Extract admin ID from token

        blacklist.setPlayerId(blacklistDTO.getPlayerId());
        blacklist.setAdminId(userId);
        blacklist.setReason(blacklistDTO.getReason());
        blacklist.setBlacklistedAt(LocalDateTime.now());
        blacklist.setBanDuration(blacklistDTO.getBanDuration());
        blacklist.setActive(true);  // Set active to true for blacklist

        blacklistRepository.save(blacklist);

        updatePlayerBlacklistStatus(blacklistDTO.getPlayerId());  // Set player status as blacklisted
        sendBlacklistNotificationEmail(blacklistDTO);
    }

    @Transactional
    public void whitelistPlayer(WhitelistDTO whitelistDTO, HttpServletRequest request) {
        // Fetch player details from the player service
        if (whitelistDTO == null) {
            throw new IllegalArgumentException("Whitelist data must not be null.");
        }

        AdminPlayerDTO adminPlayerDTO = fetchPlayerDetails(whitelistDTO.getPlayerId());

        if (!adminPlayerDTO.isBlacklisted()) {
            throw new InvalidBlacklistOperationException(
                    "Player with ID " + whitelistDTO.getPlayerId() + " is not blacklisted.");
        }

        whitelistDTO.setUsername(adminPlayerDTO.getUsername());
        whitelistDTO.setEmail(adminPlayerDTO.getEmail());

        Blacklist blacklist = blacklistRepository.findByPlayerId(whitelistDTO.getPlayerId())
                .orElseThrow(() -> new InvalidBlacklistOperationException(
                        "Player with ID " + whitelistDTO.getPlayerId() + " is not blacklisted."));

        String jwtToken = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);  // Extract admin ID from token

        blacklist.setWhitelistedAt(LocalDateTime.now());
        blacklist.setAdminId(userId);
        blacklist.setReason(whitelistDTO.getReason());
        blacklist.setActive(false);  // Set active to false for whitelist

        blacklistRepository.save(blacklist);

        updatePlayerWhitelistStatus(whitelistDTO.getPlayerId());  // Set player status as not blacklisted
        sendWhitelistNotificationEmail(whitelistDTO);
    }

    @Scheduled(fixedRate = 3600000)  // Runs every hour (3600000 ms)
    public void autoWhitelistExpiredBans() {
        LocalDateTime now = LocalDateTime.now();

        List<Blacklist> expiredBans = blacklistRepository.findAllByIsActiveTrue();

        for (Blacklist blacklist : expiredBans) {
            LocalDateTime banExpiration = blacklist.getBlacklistedAt().plusHours(blacklist.getBanDuration());

            if (now.isAfter(banExpiration)) {
                whitelistPlayerAfterExpiry(blacklist);
            }
        }
    }

    private void whitelistPlayerAfterExpiry(Blacklist blacklist) {
        blacklist.setActive(false);
        blacklist.setWhitelistedAt(LocalDateTime.now());
        blacklist.setReason("Duration expired.");
        blacklistRepository.save(blacklist);

        updatePlayerWhitelistStatus(blacklist.getPlayerId());

        AdminPlayerDTO adminPlayerDTO = fetchPlayerDetails(blacklist.getPlayerId());
        sendWhitelistNotificationEmail(new WhitelistDTO(
                adminPlayerDTO.getId(),
                adminPlayerDTO.getEmail(),
                adminPlayerDTO.getUsername(),
                blacklist.getReason())
        );
    }

    private AdminPlayerDTO fetchPlayerDetails(Long playerId) {
        // Make an API call to Player Service to get player details
        return webClientBuilder.build()
                .get()
                .uri("http://player-service:8081/api/v1/player/" + playerId + "/admin-details")
                .retrieve()
                .bodyToMono(AdminPlayerDTO.class)
                .block();
    }

    private void updatePlayerBlacklistStatus(Long playerId) {
        webClientBuilder.build()
                .put()
                .uri("http://player-service:8081/api/v1/player/update-blacklist-status?playerId=" + playerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void updatePlayerWhitelistStatus(Long playerId) {
        webClientBuilder.build()
                .put()
                .uri("http://player-service:8081/api/v1/player/update-whitelist-status?playerId=" + playerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void sendBlacklistNotificationEmail(BlacklistDTO blacklistDTO) {
        BlacklistEmailDTO emailDTO = new BlacklistEmailDTO();
        emailDTO.setTo(blacklistDTO.getEmail());
        emailDTO.setUsername(blacklistDTO.getUsername());
        emailDTO.setReason(blacklistDTO.getReason());
        emailDTO.setBanDuration(blacklistDTO.getBanDuration());

        webClientBuilder.build()
                .post()
                .uri("http://email-service:8085/api/v1/email/send-blacklist")
                .bodyValue(emailDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void sendWhitelistNotificationEmail(WhitelistDTO whitelistDTO) {
        WhitelistEmailDTO emailDTO = new WhitelistEmailDTO();
        emailDTO.setTo(whitelistDTO.getEmail());
        emailDTO.setUsername(whitelistDTO.getUsername());
        emailDTO.setReason(whitelistDTO.getReason());

        webClientBuilder.build()
                .post()
                .uri("http://email-service:8085/api/v1/email/send-whitelist")
                .bodyValue(emailDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}