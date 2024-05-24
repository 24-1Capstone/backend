package org.example.exception;

public class AttendeeSessionNotFoundException extends RuntimeException {
    public AttendeeSessionNotFoundException(String message) {
        super(message);
    }

    public AttendeeSessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
