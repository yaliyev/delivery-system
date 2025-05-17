package de.yagub.deliverysystem.mswallet.error;

public class WalletInactiveException extends RuntimeException {
    public WalletInactiveException(String message) {
        super(message);
    }
}
