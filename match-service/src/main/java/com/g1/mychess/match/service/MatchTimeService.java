package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.UpdateMatchTimeDTO;
import com.g1.mychess.match.exception.UnauthorizedActionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;


/**
 * Service interface for handling operations related to match time updates.
 * Provides functionality to update the time of an ongoing match.
 */
public interface MatchTimeService {

    /**
     * Updates the match time for a given match.
     * <p>
     * This method updates the match time for a specific match identified by the
     * provided match ID. It checks if the requester is the tournament admin and
     * updates the match time accordingly. The update time details are taken from
     * the {@link UpdateMatchTimeDTO} object. A {@link ResponseEntity} is returned
     * indicating the success or failure of the update operation.
     * </p>
     *
     * @param matchId The unique identifier of the match whose time is to be updated.
     * @param updateDTO A data transfer object containing the new match time information.
     * @param request The HTTP request object, which may contain additional information such as user authentication details.
     * @return A {@link ResponseEntity} with a status message indicating the success or failure of the operation.
     *
     * @throws UnauthorizedActionException If the current user is not authorized to update the match time (not the tournament admin).
     */
    ResponseEntity<String> updateMatchTime(Long matchId, UpdateMatchTimeDTO updateDTO, HttpServletRequest request);
}