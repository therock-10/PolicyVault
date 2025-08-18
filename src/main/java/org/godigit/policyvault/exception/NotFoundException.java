package org.godigit.policyvault.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}