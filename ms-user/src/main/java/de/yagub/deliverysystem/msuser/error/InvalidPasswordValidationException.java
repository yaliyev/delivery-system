package de.yagub.deliverysystem.msuser.error;

public class InvalidPasswordValidationException extends RuntimeException{
    public InvalidPasswordValidationException(String message){
        super(message);
    }
}
