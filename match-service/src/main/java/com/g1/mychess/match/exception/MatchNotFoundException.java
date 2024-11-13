package com.g1.mychess.match.exception;

/**
 * Custom exception class for handling scenarios where a match is not found.
 * This exception is thrown when a requested match cannot be located in the system.
 */
public class MatchNotFoundException extends RuntimeException {

    /**
     * Constructor to create a MatchNotFoundException with a specific message.
     *
     * @param message A detailed message explaining the reason for the exception.
     */
    public MatchNotFoundException(String message) {
        super(message);
    }
}
