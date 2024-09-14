package com.g1.mychess.user.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g1.mychess.user.model.PlayerRating;

@Repository
public interface PlayerRatingRepository extends JpaRepository<PlayerRating, Long>{
	Optional<PlayerRating> findByPlayerId(Long playerId);
}
