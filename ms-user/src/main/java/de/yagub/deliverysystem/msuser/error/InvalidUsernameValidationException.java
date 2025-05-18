package de.yagub.deliverysystem.msuser.error;

public class InvalidUsernameValidationException extends RuntimeException{
    public InvalidUsernameValidationException(String message){
        super(message);
    }
}
