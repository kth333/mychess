package com.g1.mychess.admin.repository;

import com.g1.mychess.admin.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for interacting with the Blacklist table in the database.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    /**
     * Finds a Blacklist entry by the player ID.
     *
     * @param playerId The ID of the player to search for in the blacklist.
     * @return An Optional containing the Blacklist entry if found, or empty if not.
     */
    Optional<Blacklist> findByPlayerId(Long playerId);

    /**
     * Retrieves all active Blacklist entries where the "isActive" field is true.
     *
     * @return A list of Blacklist entries that are currently active.
     */
    List<Blacklist> findAllByIsActiveTrue();
}