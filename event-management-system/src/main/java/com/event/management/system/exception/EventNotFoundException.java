package com.event.management.system.exception;
@SuppressWarnings("")
public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(String message) {
        super(message);
    }
}
