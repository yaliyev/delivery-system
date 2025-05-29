package de.yagub.deliverysystem.mswallet.error;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }
}
