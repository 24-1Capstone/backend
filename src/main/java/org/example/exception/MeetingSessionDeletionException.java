package org.example.exception;

public class MeetingSessionDeletionException extends RuntimeException {
    public MeetingSessionDeletionException(String message) {
        super(message);
    }
}