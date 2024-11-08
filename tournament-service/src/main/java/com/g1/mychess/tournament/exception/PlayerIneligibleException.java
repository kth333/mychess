package com.g1.mychess.tournament.exception;

public class PlayerIneligibleException extends RuntimeException {
    public PlayerIneligibleException(String message) {
        super(message);
    }
}