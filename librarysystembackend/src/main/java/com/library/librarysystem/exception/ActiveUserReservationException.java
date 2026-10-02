package com.library.librarysystem.exception;

public class ActiveUserReservationException extends RuntimeException {

    public ActiveUserReservationException(String message) {
        super(message);
    }
}