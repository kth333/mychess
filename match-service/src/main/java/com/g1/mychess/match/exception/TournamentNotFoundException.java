package com.g1.mychess.match.exception;

/**
 * Custom exception class for handling scenarios where a tournament is not found.
 * This exception is thrown when a requested tournament cannot be located in the system.
 */
public class TournamentNotFoundException extends RuntimeException{

    /**
     * Constructor to create a TournamentNotFoundException with a specific message.
     *
     * @param msg A detailed message explaining the reason for the exception.
     */
    public TournamentNotFoundException(String msg) {
        super(msg);
    }
}
