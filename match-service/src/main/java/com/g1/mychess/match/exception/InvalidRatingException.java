package com.g1.mychess.match.exception;

//just to catch any issues with the calculations!
// haven't altered the code in glicko2 service thing yet to perform the catch
public class InvalidRatingException extends RuntimeException{
    public InvalidRatingException(String msg) {
        super(msg);
    }
}