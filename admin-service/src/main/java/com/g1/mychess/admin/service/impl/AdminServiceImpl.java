// AdminServiceImpl.java
package com.g1.mychess.admin.service.impl;

import com.g1.mychess.admin.client.EmailServiceClient;
import com.g1.mychess.admin.client.PlayerServiceClient;
import com.g1.mychess.admin.dto.*;
import com.g1.mychess.admin.exception.AdminNotFoundException;
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

/**
 * Implementation of the AdminService interface, providing administrative operations such as
 * blacklisting and whitelisting players, and managing blacklist expiration.
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BlacklistRepository blacklistRepository;
    private final PlayerServiceClient playerServiceClient;
    private final EmailServiceClient emailServiceClient;
    private final AuthenticationService authenticationService;

    /**
     * Constructor to initialize the AdminServiceImpl with required dependencies.
     *
     * @param adminRepository Admin repository for accessing admin data
     * @param blacklistRepository Blacklist repository for accessing blacklist data
     * @param playerServiceClient Client for accessing player data
     * @param emailServiceClient Client for sending emails
     * @param authenticationService Service for user authentication
     */
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

    /**
     * Retrieves an admin by username.
     *
     * @param username The username of the admin
     * @return UserDTO containing the admin's details
     * @throws AdminNotFoundException if the admin with the given username is not found
     */
    @Override
    public UserDTO findAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with username: " + username));

        return AdminMapper.toUserDTO(admin);
    }

    /**
     * Blacklists a player based on the provided blacklist information.
     *
     * @param blacklistDTO DTO containing blacklist information
     * @param request The HttpServletRequest to extract the admin ID
     * @throws InvalidBlacklistOperationException if the player is already blacklisted
     */
    @Override
    @Transactional
    public void blacklistPlayer(BlacklistDTO blacklistDTO, HttpServletRequest request) {
        validatePlayerNotBlacklisted(blacklistDTO);
        PlayerDTO playerDTO = getPlayerOrThrow(blacklistDTO.getPlayerId());

        populateBlacklistDTOWithPlayerInfo(blacklistDTO, playerDTO);

        Blacklist blacklist = blacklistRepository.findByPlayerId(blacklistDTO.getPlayerId())
                .orElse(new Blacklist());

        Long adminId = authenticationService.getUserIdFromRequest(request);

        updateBlacklist(blacklist, blacklistDTO, adminId);

        blacklistRepository.save(blacklist);

        playerServiceClient.updatePlayerBlacklistStatus(blacklistDTO.getPlayerId());
        sendBlacklistNotificationEmail(blacklistDTO);
    }

    /**
     * Whitelists a player based on the provided whitelist information.
     *
     * @param whitelistDTO DTO containing whitelist information
     * @param request The HttpServletRequest to extract the admin ID
     * @throws InvalidBlacklistOperationException if the player is not blacklisted
     */
    @Override
    @Transactional
    public void whitelistPlayer(WhitelistDTO whitelistDTO, HttpServletRequest request) {
        if (whitelistDTO == null) {
            throw new IllegalArgumentException("Whitelist data must not be null.");
        }

        validatePlayerIsBlacklisted(whitelistDTO);

        PlayerDTO playerDTO = getPlayerOrThrow(whitelistDTO.getPlayerId());

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

    /**
     * Automatically whitelists players whose bans have expired.
     * This method is scheduled to run periodically.
     */
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

    /**
     * Whitelists a player whose ban has expired.
     *
     * @param blacklist The blacklist entry of the player
     */
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

    /**
     * Populates a BlacklistDTO with player information.
     *
     * @param blacklistDTO The DTO to be populated
     * @param playerDTO The player details
     */
    private void populateBlacklistDTOWithPlayerInfo(BlacklistDTO blacklistDTO, PlayerDTO playerDTO) {
        blacklistDTO.setUsername(playerDTO.getUsername());
        blacklistDTO.setEmail(playerDTO.getEmail());
    }

    /**
     * Populates a WhitelistDTO with player information.
     *
     * @param whitelistDTO The DTO to be populated
     * @param playerDTO The player details
     */
    private void populateWhitelistDTOWithPlayerInfo(WhitelistDTO whitelistDTO, PlayerDTO playerDTO) {
        whitelistDTO.setUsername(playerDTO.getUsername());
        whitelistDTO.setEmail(playerDTO.getEmail());
    }

    /**
     * Updates a blacklist entry with information from a BlacklistDTO.
     *
     * @param blacklist The blacklist entry to update
     * @param blacklistDTO The DTO containing updated information
     * @param adminId The ID of the admin performing the action
     */
    private void updateBlacklist(Blacklist blacklist, BlacklistDTO blacklistDTO, Long adminId) {
        blacklist.setPlayerId(blacklistDTO.getPlayerId());
        blacklist.setAdminId(adminId);
        blacklist.setReason(blacklistDTO.getReason());
        blacklist.setBlacklistedAt(LocalDateTime.now());
        blacklist.setBanDuration(blacklistDTO.getBanDuration());
        blacklist.setActive(true);
    }

    /**
     * Updates a blacklist entry to reflect that the player has been whitelisted.
     *
     * @param blacklist The blacklist entry to update
     * @param whitelistDTO The DTO containing whitelist information
     * @param adminId The ID of the admin performing the action
     */
    private void updateWhitelist(Blacklist blacklist, WhitelistDTO whitelistDTO, Long adminId) {
        blacklist.setWhitelistedAt(LocalDateTime.now());
        blacklist.setAdminId(adminId);
        blacklist.setReason(whitelistDTO.getReason());
        blacklist.setActive(false);
    }

    /**
     * Sends a notification email to a player when they are blacklisted.
     *
     * @param blacklistDTO The DTO containing the player's information
     */
    private void sendBlacklistNotificationEmail(BlacklistDTO blacklistDTO) {
        BlacklistEmailDTO emailDTO = new BlacklistEmailDTO();
        emailDTO.setTo(blacklistDTO.getEmail());
        emailDTO.setUsername(blacklistDTO.getUsername());
        emailDTO.setReason(blacklistDTO.getReason());
        emailDTO.setBanDuration(blacklistDTO.getBanDuration());

        emailServiceClient.sendBlacklistNotificationEmail(emailDTO);
    }

    /**
     * Sends a notification email to a player when they are whitelisted.
     *
     * @param whitelistDTO The DTO containing the player's information
     */
    private void sendWhitelistNotificationEmail(WhitelistDTO whitelistDTO) {
        WhitelistEmailDTO emailDTO = new WhitelistEmailDTO();
        emailDTO.setTo(whitelistDTO.getEmail());
        emailDTO.setUsername(whitelistDTO.getUsername());
        emailDTO.setReason(whitelistDTO.getReason());

        emailServiceClient.sendWhitelistNotificationEmail(emailDTO);
    }

    /**
     * Validates that a player is not already blacklisted.
     *
     * @param blacklistDTO The DTO containing the player's ID
     * @throws InvalidBlacklistOperationException if the player is already blacklisted
     */
    private void validatePlayerNotBlacklisted(BlacklistDTO blacklistDTO) {
        PlayerDTO playerDTO = playerServiceClient.getPlayerDetails(blacklistDTO.getPlayerId());
        if (playerDTO == null) {
            throw new IllegalArgumentException("Player not found with ID: " + blacklistDTO.getPlayerId());
        }
        if (playerDTO.isBlacklisted()) {
            throw new InvalidBlacklistOperationException(
                    "Player with ID " + blacklistDTO.getPlayerId() + " is already blacklisted.");
        }
    }

    /**
     * Validates that a player is blacklisted before whitelisting them.
     *
     * @param whitelistDTO The DTO containing the player's ID
     * @throws InvalidBlacklistOperationException if the player is not blacklisted
     */
    private void validatePlayerIsBlacklisted(WhitelistDTO whitelistDTO) {
        PlayerDTO playerDTO = playerServiceClient.getPlayerDetails(whitelistDTO.getPlayerId());
        if (playerDTO == null) {
            throw new IllegalArgumentException("Player not found with ID: " + whitelistDTO.getPlayerId());
        }
        if (!playerDTO.isBlacklisted()) {
            throw new InvalidBlacklistOperationException(
                    "Player with ID " + whitelistDTO.getPlayerId() + " is not blacklisted.");
        }
    }

    /**
     * Retrieves the player details for the given player ID.
     * If the player does not exist, throws an {@link IllegalArgumentException}.
     *
     * @param playerId the ID of the player to retrieve
     * @return the {@link PlayerDTO} containing player details
     * @throws IllegalArgumentException if no player is found with the given ID
     */
    private PlayerDTO getPlayerOrThrow(Long playerId) {
        PlayerDTO playerDTO = playerServiceClient.getPlayerDetails(playerId);
        if (playerDTO == null) {
            throw new IllegalArgumentException("Player not found with ID: " + playerId);
        }
        return playerDTO;
    }
}
