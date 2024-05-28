package org.example.exception;

public class AttendeeSessionCreationException extends RuntimeException {
    public AttendeeSessionCreationException(String message) {
        super(message);
    }

    public AttendeeSessionCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
