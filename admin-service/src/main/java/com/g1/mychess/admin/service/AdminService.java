package com.g1.mychess.admin.service;

import com.g1.mychess.admin.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;
import com.g1.mychess.admin.exception.*;

/**
 * Service interface for administrative actions in the system. It includes functionalities
 * for managing players' blacklist and whitelist statuses, as well as automatic whitelisting
 * of expired bans.
 */
public interface AdminService {

    /**
     * Retrieves the admin details by their username.
     *
     * @param username The username of the admin to be retrieved.
     * @return A UserDTO object containing the admin's details.
     * @throws AdminNotFoundException If no admin is found with the given username.
     */
    UserDTO findAdminByUsername(String username);


    /**
     * Blacklists a player based on the provided blacklist data.
     * The player is marked as blacklisted and the blacklist record is saved.
     *
     * @param blacklistDTO The data transfer object containing the player ID, reason for blacklisting,
     *                     and the ban duration.
     * @param request The HTTP request to get the admin's details who is performing the action.
     * @throws InvalidBlacklistOperationException If the player is already blacklisted or the blacklist operation is invalid.
     */
    void blacklistPlayer(BlacklistDTO blacklistDTO, HttpServletRequest request);

    /**
     * Whitelists a player by removing them from the blacklist.
     * The player's record is updated, and the whitelist status is saved.
     *
     * @param whitelistDTO The data transfer object containing the player ID, reason for whitelisting.
     * @param request The HTTP request to get the admin's details who is performing the action.
     * @throws InvalidBlacklistOperationException If the player is not blacklisted and cannot be whitelisted.
     */
    void whitelistPlayer(WhitelistDTO whitelistDTO, HttpServletRequest request);

    /**
     * Automatically whitelists players whose bans have expired.
     * This method runs every hour (as configured by the @Scheduled annotation).
     * It checks all active blacklist records and whitelists the players whose ban duration has expired.
     */
    @Scheduled(fixedRate = 3600000)  // Runs every hour (3600000 ms)
    void autoWhitelistExpiredBans();
}