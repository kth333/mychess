package com.g1.mychess.match.exception;

/**
 * Custom exception class for unauthorized actions in the application.
 * This exception is thrown when a user attempts to perform an action
 * that they do not have permission for.
 */
public class UnauthorizedActionException extends RuntimeException {

    /**
     * Constructor to create an UnauthorizedActionException with a specific message.
     *
     * @param message A detailed message explaining the reason for the exception.
     */
    public UnauthorizedActionException(String message) {
        super(message);
    }
    
}
