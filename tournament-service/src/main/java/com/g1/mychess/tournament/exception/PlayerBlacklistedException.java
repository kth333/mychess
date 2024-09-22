package com.g1.mychess.tournament.exception;

public class PlayerBlacklistedException extends RuntimeException {
    public PlayerBlacklistedException(String message) {
        super(message);
    }
}
