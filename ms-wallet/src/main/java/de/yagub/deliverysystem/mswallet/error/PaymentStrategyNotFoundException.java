package de.yagub.deliverysystem.mswallet.error;

public class PaymentStrategyNotFoundException extends RuntimeException {
    public PaymentStrategyNotFoundException(String message) {
        super(message);
    }
}
