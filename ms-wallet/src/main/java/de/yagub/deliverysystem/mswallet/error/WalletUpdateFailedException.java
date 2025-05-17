package de.yagub.deliverysystem.mswallet.error;

public class WalletUpdateFailedException extends RuntimeException{
    public WalletUpdateFailedException(String message){
        super(message);
    }
}
