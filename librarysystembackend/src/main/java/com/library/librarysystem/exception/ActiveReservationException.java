package com.library.librarysystem.exception;

public class ActiveReservationException extends RuntimeException {

    public ActiveReservationException(String message) {
        super(message);
    }
}