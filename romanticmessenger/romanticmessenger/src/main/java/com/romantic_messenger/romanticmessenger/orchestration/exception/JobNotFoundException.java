package com.romantic_messenger.romanticmessenger.orchestration.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
