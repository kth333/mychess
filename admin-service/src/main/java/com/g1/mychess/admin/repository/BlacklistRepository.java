package com.g1.mychess.admin.repository;

import com.g1.mychess.admin.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    Optional<Blacklist> findByPlayerId(Long playerId);

    List<Blacklist> findAllByIsActiveTrue();
}