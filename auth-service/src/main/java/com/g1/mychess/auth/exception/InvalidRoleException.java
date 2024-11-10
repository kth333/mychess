package com.g1.mychess.auth.exception;

/**
 * Exception thrown when an invalid role is encountered.
 */
public class InvalidRoleException extends RuntimeException {

    /**
     * Constructs a new InvalidRoleException with the specified error message.
     *
     * @param message the error message
     */
    public InvalidRoleException(String message) {
        super(message);
    }
}
