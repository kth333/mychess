package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a user attempts to perform an action they are not authorized for.
 */
public class UnauthorizedActionException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedActionException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
