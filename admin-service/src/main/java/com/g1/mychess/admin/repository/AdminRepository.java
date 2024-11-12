package com.g1.mychess.admin.repository;

import com.g1.mychess.admin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for interacting with the Admin table in the database.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Finds an Admin entry by the username.
     *
     * @param username The username of the admin to search for.
     * @return An Optional containing the Admin entry if found, or empty if not.
     */
    Optional<Admin> findByUsername(String username);
}
