package org.godigit.policyvault.exception;

public class PolicyNotFoundException extends RuntimeException{
    public PolicyNotFoundException(String message) {
        super(message);
    }

}
