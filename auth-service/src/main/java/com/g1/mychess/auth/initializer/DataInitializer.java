package com.g1.mychess.auth.initializer;

import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * This class initializes user tokens in the database if none exist.
 * It generates dummy data for development, testing, and demonstration purposes.
 * The tokens are created with expiration times, token types, and user roles.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserTokenRepository userTokenRepository;

    /**
     * Runs on application startup to check if user tokens are already initialized.
     * If not, it initializes dummy user tokens for testing and development.
     *
     * @param args command-line arguments (not used here)
     */
    @Override
    public void run(String... args) {
        if(userTokenRepository.count() == 0){
            initializeUserTokens();
            System.out.println("User tokens initialized successfully!");
        } else {
            System.out.println("User tokens already exist in the database.");
        }
    }

    /**
     * Initializes dummy user tokens for development and testing.
     * Tokens are created for both ROLE_ADMIN and ROLE_PLAYER with unique values.
     * All tokens have their 'used' status set to true for testing purposes.
     */
    private void initializeUserTokens() {
        Set<UserToken> userTokens = new HashSet<>();

        // Base token string
        String baseToken = "522gb040-1a81-43ac-839f-c86ce5ac029b";

        // Creating tokens for ROLE_ADMIN (IDs 1-3)
        for (long i = 1; i <= 3; i++) {
            // Modify one character in the base token for uniqueness
            String modifiedToken = modifyToken(baseToken, i);
            userTokens.add(new UserToken(
                    modifiedToken, // unique token
                    LocalDateTime.now().plusDays(1), // expiration time
                    UserToken.TokenType.EMAIL_VERIFICATION,
                    i, // userId
                    "ROLE_ADMIN"
            ));
        }

        // Creating tokens for ROLE_PLAYER (IDs 1-8)
        for (long i = 1; i <= 8; i++) {
            // Modify one character in the base token for uniqueness
            String modifiedToken = modifyToken(baseToken, i + 3);
            userTokens.add(new UserToken(
                    modifiedToken, // unique token
                    LocalDateTime.now().plusDays(1), // expiration time
                    UserToken.TokenType.EMAIL_VERIFICATION,
                    i,
                    "ROLE_PLAYER"
            ));
        }

        // Set the used status to true for all tokens
        userTokens.forEach(token -> token.setUsed(true));

        // Save the user tokens to the database
        userTokenRepository.saveAll(userTokens);
        System.out.println("User tokens initialized");
    }

    /**
     * Modifies a base token string to ensure uniqueness by changing one character
     * based on the given index.
     * <p>
     * This is used to create distinct tokens for different users.
     * </p>
     *
     * @param token the base token to modify
     * @param index the index used to modify the token for uniqueness
     * @return a new token string with one character changed for uniqueness
     */
    private String modifyToken(String token, long index) {
        // Convert the token to a char array to modify it
        char[] tokenChars = token.toCharArray();

        // Change one character based on the index (ensuring it's within bounds)
        int positionToChange = (int) (index % tokenChars.length);
        tokenChars[positionToChange] = (char) ((tokenChars[positionToChange] + 1 - '0') % 10 + '0'); // Change a digit

        return new String(tokenChars);
    }
}
