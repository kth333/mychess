package com.g1.mychess.auth.exception;

/**
 * Exception thrown when there is an issue with a user token.
 */
public class UserTokenException extends RuntimeException {

    /**
     * Constructs a new UserTokenException with the specified error message.
     *
     * @param message the error message
     */
    public UserTokenException(String message) {
        super(message);
    }
}
