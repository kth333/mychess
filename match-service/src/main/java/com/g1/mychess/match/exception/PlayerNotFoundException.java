package com.g1.mychess.match.exception;

/**
 * Custom exception class for handling scenarios where a player is not found.
 * This exception is thrown when a requested player cannot be located in the system.
 */
public class PlayerNotFoundException extends RuntimeException {

    /**
     * Constructor to create a PlayerNotFoundException with a specific message.
     *
     * @param message A detailed message explaining the reason for the exception.
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}