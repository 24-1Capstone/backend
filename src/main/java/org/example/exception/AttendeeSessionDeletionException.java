package org.example.exception;

public class AttendeeSessionDeletionException extends RuntimeException {
    public AttendeeSessionDeletionException(String message) {
        super(message);
    }

    public AttendeeSessionDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
