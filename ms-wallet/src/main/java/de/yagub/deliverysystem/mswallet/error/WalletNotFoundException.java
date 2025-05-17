package de.yagub.deliverysystem.mswallet.error;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException(String message){
        super(message);
    }
}
