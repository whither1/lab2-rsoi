package org.lab2.bonus.exception;

public class PrivilegeNotFoundException extends RuntimeException {

    public PrivilegeNotFoundException(String username) {
        super("Privilege not found: " + username);
    }
}
