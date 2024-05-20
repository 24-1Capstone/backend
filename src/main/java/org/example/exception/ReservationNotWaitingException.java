package org.example.exception;

public class ReservationNotWaitingException extends RuntimeException {

    public ReservationNotWaitingException(String message) {
        super(message);
    }
}
