package com.g1.mychess.admin.exception;

/**
 * Custom exception thrown when an invalid operation is attempted on the blacklist.
 * This exception is used to indicate that a requested operation on the blacklist
 * (e.g., adding, removing, or updating entries) is not valid or allowed.
 */
public class InvalidBlacklistOperationException extends RuntimeException {

    /**
     * Constructor for InvalidBlacklistOperationException.
     *
     * @param message the detail message to be associated with the exception
     */
    public InvalidBlacklistOperationException(String message) {
        super(message);
    }
}
