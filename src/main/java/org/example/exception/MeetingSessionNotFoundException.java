package org.example.exception;

public class MeetingSessionNotFoundException extends RuntimeException {
    public MeetingSessionNotFoundException(String message) {
        super(message);
    }
}
