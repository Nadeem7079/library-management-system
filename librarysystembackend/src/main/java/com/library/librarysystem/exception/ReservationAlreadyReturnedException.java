package com.library.librarysystem.exception;

public class ReservationAlreadyReturnedException extends RuntimeException {

    public ReservationAlreadyReturnedException(String message) {
        super(message);
    }
}