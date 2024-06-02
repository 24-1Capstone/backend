package org.example.exception;

public class MeetingAlreadyExistsException extends RuntimeException {
    public MeetingAlreadyExistsException(String message) {
        super(message);
    }
}
