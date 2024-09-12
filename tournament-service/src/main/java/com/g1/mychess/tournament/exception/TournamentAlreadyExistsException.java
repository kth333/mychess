package com.g1.mychess.tournament.exception;

public class TournamentAlreadyExistsException extends RuntimeException {
    
    public TournamentAlreadyExistsException(String message) {
        super(message);
    }
}
