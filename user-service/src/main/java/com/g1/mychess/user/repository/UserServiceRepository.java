package com.g1.mychess.user.repository;

// Importing necessary packages
import com.g1.mychess.user.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indicates that this interface is a Spring Data repository.
public interface UserServiceRepository extends JpaRepository<User, Long>{
    
}
/**
 * Repository interface for Department entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */