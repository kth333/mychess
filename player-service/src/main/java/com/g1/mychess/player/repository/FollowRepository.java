package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.Follow;
import com.g1.mychess.player.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Follow} entities.
 * Provides methods to query and interact with player follow relationships in the database.
 */
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * Retrieves a paginated list of {@link Follow} entries where the given player is the follower.
     *
     * @param follower the player who is following others.
     * @param pageable the pagination information (page number, page size, etc.).
     * @return a {@link Page} containing {@link Follow} entries where the given player is the follower.
     */
    Page<Follow> findByFollower(Player follower, Pageable pageable);

    /**
     * Retrieves a paginated list of {@link Follow} entries where the given player is the followed.
     *
     * @param followed the player being followed by others.
     * @param pageable the pagination information (page number, page size, etc.).
     * @return a {@link Page} containing {@link Follow} entries where the given player is the followed.
     */
    Page<Follow> findByFollowed(Player followed, Pageable pageable);

    /**
     * Retrieves a {@link Follow} entry for a specific follower and followed player.
     *
     * @param follower the player who is following.
     * @param followed the player who is being followed.
     * @return an {@link Optional} containing the {@link Follow} entry if it exists, or empty if not found.
     */
    Optional<Follow> findByFollowerAndFollowed(Player follower, Player followed);
}
