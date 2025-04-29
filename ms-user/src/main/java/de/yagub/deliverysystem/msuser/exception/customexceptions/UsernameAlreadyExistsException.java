package de.yagub.deliverysystem.msuser.exception.customexceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' already exists");
    }
}
