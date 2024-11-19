package com.g1.mychess.tournament.exception;

/**
 * Exception thrown when a player attempts to register outside the allowed registration period for a tournament.
 */
public class RegistrationPeriodException extends RuntimeException {

    /**
     * Constructs a new RegistrationPeriodException with the specified detail message.
     *
     * @param message the detail message
     */
    public RegistrationPeriodException(String message) {
        super(message);
    }
}
