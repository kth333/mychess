package com.g1.mychess.admin.exception;

/**
 * Custom exception thrown when a user attempts an unauthorized action.
 * This exception is used to indicate that the action being performed
 * is not allowed for the current user due to insufficient permissions.
 */
public class UnauthorizedActionException extends RuntimeException {

    /**
     * Constructor for UnauthorizedActionException.
     *
     * @param message the detail message to be associated with the exception
     */
    public UnauthorizedActionException(String message) {
        super(message);
    }
    
}