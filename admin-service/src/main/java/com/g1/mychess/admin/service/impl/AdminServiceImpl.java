// AdminServiceImpl.java
package com.g1.mychess.admin.service.impl;

import com.g1.mychess.admin.client.EmailServiceClient;
import com.g1.mychess.admin.client.PlayerServiceClient;
import com.g1.mychess.admin.dto.*;
import com.g1.mychess.admin.exception.InvalidBlacklistOperationException;
import com.g1.mychess.admin.mapper.AdminMapper;
import com.g1.mychess.admin.model.Admin;
import com.g1.mychess.admin.model.Blacklist;
import com.g1.mychess.admin.repository.AdminRepository;
import com.g1.mychess.admin.repository.BlacklistRepository;
import com.g1.mychess.admin.service.AdminService;
import com.g1.mychess.admin.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BlacklistRepository blacklistRepository;
    private final PlayerServiceClient playerServiceClient;
    private final EmailServiceClient emailServiceClient;
    private final AuthenticationService authenticationService;

    public AdminServiceImpl(
            AdminRepository adminRepository,
            BlacklistRepository blacklistRepository,
            PlayerServiceClient playerServiceClient,
            EmailServiceClient emailServiceClient,
            AuthenticationService authenticationService
    ) {
        this.adminRepository = adminRepository;
        this.blacklistRepository = blacklistRepository;
        this.playerServiceClient = playerServiceClient;
        this.emailServiceClient = emailServiceClient;
        this.authenticationService = authenticationService;
    }

    @Override
    public UserDTO findAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with username: " + username));

        return AdminMapper.toUserDTO(admin);
    }

    @Override
    @Transactional
    public void blacklistPlayer(BlacklistDTO blacklistDTO, HttpServletRequest request) {
        PlayerDTO playerDTO = playerServiceClient.getPlayerDetails(blacklistDTO.getPlayerId());

        if (playerDTO.isBlacklisted()) {
            throw new InvalidBlacklistOperationException(
                    "Player with ID " + blacklistDTO.getPlayerId() + " is already blacklisted.");
        }

        populateBlacklistDTOWithPlayerInfo(blacklistDTO, playerDTO);

        Blacklist blacklist = blacklistRepository.findByPlayerId(blacklistDTO.getPlayerId())
                .orElse(new Blacklist());

        Long adminId = authenticationService.getUserIdFromRequest(request);

        updateBlacklist(blacklist, blacklistDTO, adminId);

        blacklistRepository.save(blacklist);

        playerServiceClient.updatePlayerBlacklistStatus(blacklistDTO.getPlayerId());
        sendBlacklistNotificationEmail(blacklistDTO);
    }

    @Override
    @Transactional
    public void whitelistPlayer(WhitelistDTO whitelistDTO, HttpServletRequest request) {
        if (whitelistDTO == null) {
            throw new IllegalArgumentException("Whitelist data must not be null.");
        }

        PlayerDTO playerDTO = playerServiceClient.getPlayerDetails(whitelistDTO.getPlayerId());

        if (!playerDTO.isBlacklisted()) {
            throw new InvalidBlacklistOperationException(
                    "Player with ID " + whitelistDTO.getPlayerId() + " is not blacklisted.");
        }

        populateWhitelistDTOWithPlayerInfo(whitelistDTO, playerDTO);

        Blacklist blacklist = blacklistRepository.findByPlayerId(whitelistDTO.getPlayerId())
                .orElseThrow(() -> new InvalidBlacklistOperationException(
                        "Player with ID " + whitelistDTO.getPlayerId() + " is not blacklisted."));

        Long adminId = authenticationService.getUserIdFromRequest(request);

        updateWhitelist(blacklist, whitelistDTO, adminId);

        blacklistRepository.save(blacklist);

        playerServiceClient.updatePlayerWhitelistStatus(whitelistDTO.getPlayerId());
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

        playerServiceClient.updatePlayerWhitelistStatus(blacklist.getPlayerId());

        PlayerDTO playerDTO = playerServiceClient.getPlayerDetails(blacklist.getPlayerId());
        WhitelistDTO whitelistDTO = new WhitelistDTO(
                playerDTO.getId(),
                playerDTO.getEmail(),
                playerDTO.getUsername(),
                blacklist.getReason()
        );
        sendWhitelistNotificationEmail(whitelistDTO);
    }

    private void populateBlacklistDTOWithPlayerInfo(BlacklistDTO blacklistDTO, PlayerDTO playerDTO) {
        blacklistDTO.setUsername(playerDTO.getUsername());
        blacklistDTO.setEmail(playerDTO.getEmail());
    }

    private void populateWhitelistDTOWithPlayerInfo(WhitelistDTO whitelistDTO, PlayerDTO playerDTO) {
        whitelistDTO.setUsername(playerDTO.getUsername());
        whitelistDTO.setEmail(playerDTO.getEmail());
    }

    private void updateBlacklist(Blacklist blacklist, BlacklistDTO blacklistDTO, Long adminId) {
        blacklist.setPlayerId(blacklistDTO.getPlayerId());
        blacklist.setAdminId(adminId);
        blacklist.setReason(blacklistDTO.getReason());
        blacklist.setBlacklistedAt(LocalDateTime.now());
        blacklist.setBanDuration(blacklistDTO.getBanDuration());
        blacklist.setActive(true);
    }

    private void updateWhitelist(Blacklist blacklist, WhitelistDTO whitelistDTO, Long adminId) {
        blacklist.setWhitelistedAt(LocalDateTime.now());
        blacklist.setAdminId(adminId);
        blacklist.setReason(whitelistDTO.getReason());
        blacklist.setActive(false);
    }

    private void sendBlacklistNotificationEmail(BlacklistDTO blacklistDTO) {
        BlacklistEmailDTO emailDTO = new BlacklistEmailDTO();
        emailDTO.setTo(blacklistDTO.getEmail());
        emailDTO.setUsername(blacklistDTO.getUsername());
        emailDTO.setReason(blacklistDTO.getReason());
        emailDTO.setBanDuration(blacklistDTO.getBanDuration());

        emailServiceClient.sendBlacklistNotificationEmail(emailDTO);
    }

    private void sendWhitelistNotificationEmail(WhitelistDTO whitelistDTO) {
        WhitelistEmailDTO emailDTO = new WhitelistEmailDTO();
        emailDTO.setTo(whitelistDTO.getEmail());
        emailDTO.setUsername(whitelistDTO.getUsername());
        emailDTO.setReason(whitelistDTO.getReason());

        emailServiceClient.sendWhitelistNotificationEmail(emailDTO);
    }
}
