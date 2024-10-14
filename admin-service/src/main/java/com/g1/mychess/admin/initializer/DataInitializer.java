package com.g1.mychess.admin.initializer;

import com.g1.mychess.admin.model.Admin;
import com.g1.mychess.admin.repository.AdminRepository; // Adjust the package as necessary
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        reCreateDatabase();
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

    private void reCreateDatabase() {
        // Adjust the SQL command according to your database
        String dropSQL = "DROP DATABASE tournament_service_db;";
        String createSQL = "CREATE DATABASE tournament_service_db;";
        try {
            jdbcTemplate.execute(dropSQL);
            jdbcTemplate.execute(createSQL);
            System.out.println("Database recreated successfully.");
        } catch (Exception e) {
            System.err.println("Failed to drop database: " + e.getMessage());
        }
    }

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
