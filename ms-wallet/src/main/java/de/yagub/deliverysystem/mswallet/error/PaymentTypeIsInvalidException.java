package de.yagub.deliverysystem.mswallet.error;

public class PaymentTypeIsInvalidException extends RuntimeException{
    public PaymentTypeIsInvalidException(String message){
        super(message);
    }
}
