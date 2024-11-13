package com.g1.mychess.player.repository;

import com.g1.mychess.player.model.Follow;
import com.g1.mychess.player.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Page<Follow> findByFollower(Player follower, Pageable pageable);
    Page<Follow> findByFollowed(Player followed, Pageable pageable);
    Optional<Follow> findByFollowerAndFollowed(Player follower, Player followed);
}
