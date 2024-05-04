package org.example.exception;

public class AttendeeAlreadyExistsException extends RuntimeException{

    public AttendeeAlreadyExistsException(String message){
        super(message);
    }
}
