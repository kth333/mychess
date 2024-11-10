package com.g1.mychess.auth.exception;

/**
 * Exception thrown when an email is invalid.
 */
public class InvalidEmailException extends RuntimeException {

    /**
     * Constructs a new InvalidEmailException with the specified error message.
     *
     * @param message the error message
     */
    public InvalidEmailException(String message) {
        super(message);
    }
}
