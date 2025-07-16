package de.yagub.deliverysystem.mswallet.error;

public class InvalidPaymentRequestException extends RuntimeException{
    public InvalidPaymentRequestException(String message){
        super(message);
    }
}
