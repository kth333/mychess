package com.g1.mychess.tournament.repository;

import com.g1.mychess.tournament.model.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing {@link Tournament} entities.
 * Provides CRUD operations and custom queries related to tournaments.
 */
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    /**
     * Finds a tournament by its name.
     *
     * @param name the name of the tournament
     * @return an {@link Optional} containing the {@link Tournament} if found, or an empty Optional
     */
    Optional<Tournament> findByName(String name);

    /**
     * Finds a tournament by its ID.
     *
     * @param id the tournament's ID
     * @return an {@link Optional} containing the {@link Tournament} if found, or an empty Optional
     */
    Optional<Tournament> findById(Long id);

    /**
     * Finds all tournaments where the registration start date is before the specified date
     * and the tournament status matches the provided status.
     *
     * @param dateTime the date to compare the registration start date against
     * @param status the tournament status to filter by
     * @return a list of {@link Tournament}s that match the criteria
     */
    List<Tournament> findByRegistrationStartDateBeforeAndStatus(LocalDateTime dateTime, Tournament.TournamentStatus status);

    /**
     * Finds tournaments that start after the specified date and returns them in ascending order of their start time.
     *
     * @param date the date to compare the start time against
     * @param pageable the pagination information
     * @return a {@link Page} of {@link Tournament}s starting after the specified date
     */
    Page<Tournament> findByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime date, Pageable pageable);

    /**
     * Finds all tournaments and returns them in ascending order of their start time.
     *
     * @param pageable the pagination information
     * @return a {@link Page} of all {@link Tournament}s ordered by start date
     */
    Page<Tournament> findAllByOrderByStartDateTimeAsc(Pageable pageable);
}
