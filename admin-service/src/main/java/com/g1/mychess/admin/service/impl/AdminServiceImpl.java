package com.g1.mychess.admin.service.impl;

import com.g1.mychess.admin.dto.*;
import com.g1.mychess.admin.exception.InvalidBlacklistOperationException;
import com.g1.mychess.admin.model.Admin;
import com.g1.mychess.admin.model.Blacklist;
import com.g1.mychess.admin.repository.AdminRepository;
import com.g1.mychess.admin.repository.BlacklistRepository;
import com.g1.mychess.admin.service.AdminService;
import com.g1.mychess.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BlacklistRepository blacklistRepository;
    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    public AdminServiceImpl(AdminRepository adminRepository, BlacklistRepository blacklistRepository, WebClient.Builder webClientBuilder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.blacklistRepository = blacklistRepository;
        this.webClientBuilder = webClientBuilder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDTO findAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with username: " + username));

        return new UserDTO(admin.getAdminId(), admin.getUsername(), admin.getPassword(), admin.getEmail(), admin.getRole());
    }

    @Override
    @Transactional
    public void blacklistPlayer(BlacklistDTO blacklistDTO, HttpServletRequest request) {
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
        Long userId = jwtUtil.extractUserId(jwtToken);

        blacklist.setPlayerId(blacklistDTO.getPlayerId());
        blacklist.setAdminId(userId);
        blacklist.setReason(blacklistDTO.getReason());
        blacklist.setBlacklistedAt(LocalDateTime.now());
        blacklist.setBanDuration(blacklistDTO.getBanDuration());
        blacklist.setActive(true);

        blacklistRepository.save(blacklist);

        updatePlayerBlacklistStatus(blacklistDTO.getPlayerId());
        sendBlacklistNotificationEmail(blacklistDTO);
    }

    @Override
    @Transactional
    public void whitelistPlayer(WhitelistDTO whitelistDTO, HttpServletRequest request) {
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
        Long userId = jwtUtil.extractUserId(jwtToken);

        blacklist.setWhitelistedAt(LocalDateTime.now());
        blacklist.setAdminId(userId);
        blacklist.setReason(whitelistDTO.getReason());
        blacklist.setActive(false);

        blacklistRepository.save(blacklist);

        updatePlayerWhitelistStatus(whitelistDTO.getPlayerId());
        sendWhitelistNotificationEmail(whitelistDTO);
    }

    @Override
    @Scheduled(fixedRate = 3600000)
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
        return webClientBuilder.build()
                .get()
                .uri(playerServiceUrl + "/api/v1/player/" + playerId + "/admin-details")
                .retrieve()
                .bodyToMono(AdminPlayerDTO.class)
                .block();
    }

    private void updatePlayerBlacklistStatus(Long playerId) {
        webClientBuilder.build()
                .put()
                .uri(playerServiceUrl + "/api/v1/player/update-blacklist-status?playerId=" + playerId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void updatePlayerWhitelistStatus(Long playerId) {
        webClientBuilder.build()
                .put()
                .uri(playerServiceUrl + "/api/v1/player/update-whitelist-status?playerId=" + playerId)
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
                .uri(emailServiceUrl + "/api/v1/email/send-blacklist")
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
                .uri(emailServiceUrl + "/api/v1/email/send-whitelist")
                .bodyValue(emailDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}