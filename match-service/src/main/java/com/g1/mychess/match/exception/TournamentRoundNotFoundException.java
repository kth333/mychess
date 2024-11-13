package com.g1.mychess.match.exception;

/**
 * Custom exception class for handling scenarios where a tournament round is not found.
 * This exception is thrown when a requested tournament round cannot be located in the system.
 */
public class TournamentRoundNotFoundException extends RuntimeException{

    /**
     * Constructor to create a TournamentRoundNotFoundException with a specific message.
     *
     * @param message A detailed message explaining the reason for the exception.
     */
    public TournamentRoundNotFoundException(String message) {
        super(message);
    }
}
