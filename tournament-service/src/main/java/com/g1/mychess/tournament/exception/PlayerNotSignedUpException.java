package com.g1.mychess.tournament.exception;

public class PlayerNotSignedUpException extends RuntimeException {
    public PlayerNotSignedUpException(String message) {
        super(message);
    }
}