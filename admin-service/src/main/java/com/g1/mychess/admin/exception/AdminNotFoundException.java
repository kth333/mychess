package com.g1.mychess.admin.exception;

/**
 * Custom exception thrown when an admin is not found in the system.
 * This exception is used to indicate that the specified admin could not
 * be located in the database or system, usually when attempting to retrieve
 * or perform actions on a non-existent admin.
 */
public class AdminNotFoundException extends RuntimeException {

    /**
     * Constructor for AdminNotFoundException.
     *
     * @param message the detail message to be associated with the exception
     */
    public AdminNotFoundException(String message) {
        super(message);
    }
}
