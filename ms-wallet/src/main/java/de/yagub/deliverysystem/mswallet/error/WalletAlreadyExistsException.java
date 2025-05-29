package de.yagub.deliverysystem.mswallet.error;

public class WalletAlreadyExistsException extends RuntimeException{
    public WalletAlreadyExistsException(String message){
        super(message);
    }
}
