package com.g1.mychess.tournament.exception;

public class PlayerAlreadySignedUpException extends RuntimeException {
    public PlayerAlreadySignedUpException(String message) {
        super(message);
    }
}