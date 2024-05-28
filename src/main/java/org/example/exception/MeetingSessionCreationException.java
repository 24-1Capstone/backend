package org.example.exception;

public class MeetingSessionCreationException extends RuntimeException {
    public MeetingSessionCreationException(String message) {
        super(message);
    }
}
