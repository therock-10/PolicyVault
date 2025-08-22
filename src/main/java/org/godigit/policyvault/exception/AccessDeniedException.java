package org.godigit.policyvault.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Access is denied");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}