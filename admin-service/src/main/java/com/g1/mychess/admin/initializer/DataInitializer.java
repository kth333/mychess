package com.g1.mychess.admin.initializer;

import com.g1.mychess.admin.model.Admin;
import com.g1.mychess.admin.repository.AdminRepository; // Adjust the package as necessary
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for initializing default admin accounts in the database.
 * It runs on application startup to check if any admins already exist and creates default admin accounts if not.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * This method is called when the application starts. It checks if there are existing admin accounts
     * in the database. If there are no admins, it initializes default admin accounts.
     *
     * @param args command line arguments
     */
    @Override
    public void run(String... args) throws Exception {
        // Check if admins already exist to avoid duplicate entries
        if (adminRepository.count() == 0) {
            createAdmin("admin1", "admin1@example.com");
            createAdmin("admin2", "admin2@example.com");
            createAdmin("admin3", "admin3@example.com");

            System.out.println("Admin accounts initialized.");
        } else {
            System.out.println("Admin accounts already exist in the database.");
        }
    }

    /**
     * Creates and saves an admin account with the given username and email.
     * A default password is set for all admins, which should be securely hashed in a real application.
     *
     * @param username the username for the admin account
     * @param email the email for the admin account
     */
    private void createAdmin(String username, String email) {
        Admin admin = new Admin();
        admin.setUsername(username); // Set the username
        admin.setEmail(email); // Set the email
        admin.setPassword("$2a$10$/1b1aYbc9OGxNxDsuf4DJOe9QlW33cl7NAn0B.Rtd0gpthO6f2bnq"); // Set the hashed password

        // Save the admin to the database
        adminRepository.save(admin);
        System.out.println("Admin initialized with username: " + admin.getUsername());
    }
}
